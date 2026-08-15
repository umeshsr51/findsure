package com.findsure.service;

import com.findsure.dto.ItemCreateRequest;
import com.findsure.dto.ItemResponse;
import com.findsure.dto.ItemSummaryResponse;
import com.findsure.dto.ItemUpdateRequest;
import com.findsure.dto.PagedResponse;
import com.findsure.dto.PublicItemResponse;
import com.findsure.entity.Item;
import com.findsure.exception.NotFoundException;
import com.findsure.repository.ItemRepository;
import com.findsure.util.QrTokenGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final QrTokenGenerator qrTokenGenerator;
    private final String qrBaseUrl;
    private final QrImageService qrImageService;
    private final ScanService scanService;
    private static final Pattern QR_TOKEN = Pattern.compile("FS-[A-HJ-NP-Z2-9]{7}");

    public ItemService(
            ItemRepository itemRepository,
            QrTokenGenerator qrTokenGenerator,
            @Value("${app.qr.base-url}") String qrBaseUrl,
            QrImageService qrImageService,
            ScanService scanService
    ) {
        this.itemRepository = itemRepository;
        this.qrTokenGenerator = qrTokenGenerator;
        this.qrBaseUrl = qrBaseUrl;
        this.qrImageService = qrImageService;
        this.scanService = scanService;
    }

    @Transactional
    public ItemResponse createItem(Long userId, ItemCreateRequest request) {
        Item item = Item.builder()
                .userId(userId)
                .name(request.getName().trim())
                .category(request.getCategory())
                .description(request.getDescription())
                .photoUrl(request.getPhotoUrl())
                .status(Item.Status.active)
                .qrToken(qrTokenGenerator.generateUnique())
                .build();

        Item saved = itemRepository.save(item);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ItemResponse getItem(Long userId, Long itemId) {
        Item item = findOwnedItem(userId, itemId);
        return toResponse(item);
    }

    @Transactional(readOnly = true)
    public PagedResponse<ItemSummaryResponse> getItems(
            Long userId, Item.Status status, String search, int page, int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> result = itemRepository.search(userId, status, blankToNull(search), pageable);

        List<ItemSummaryResponse> items = result.getContent().stream()
                .map(item -> ItemSummaryResponse.from(item, (int) scanService.countForItem(item.getId())))
                .collect(Collectors.toList());

        return new PagedResponse<>(items, page, size, result.getTotalElements());
    }

    @Transactional
    public ItemResponse updateItem(Long userId, Long itemId, ItemUpdateRequest request) {
        Item item = findOwnedItem(userId, itemId);

        // PATCH-style: every field on ItemUpdateRequest is optional (per the
        // API spec), so only overwrite what was actually sent — null means
        // "leave untouched", not "clear this field".
        if (request.getName() != null) {
            item.setName(request.getName().trim());
        }
        if (request.getCategory() != null) {
            item.setCategory(request.getCategory());
        }
        if (request.getDescription() != null) {
            item.setDescription(request.getDescription());
        }
        if (request.getPhotoUrl() != null) {
            item.setPhotoUrl(request.getPhotoUrl());
        }

        Item saved = itemRepository.save(item);
        return toResponse(saved);
    }

    @Transactional
    public void deleteItem(Long userId, Long itemId) {
        // Soft delete only — matches the architecture's deletedAt column and
        // the "never physically delete" rule.
        Item item = findOwnedItem(userId, itemId);
        item.setDeletedAt(LocalDateTime.now());
        itemRepository.save(item);
    }

    @Transactional
    public ItemResponse markLost(Long userId, Long itemId) {
        Item item = findOwnedItem(userId, itemId);
        item.setStatus(Item.Status.lost);
        item.setLostAt(LocalDateTime.now());
        item.setFoundAt(null);
        Item saved = itemRepository.save(item);
        return toResponse(saved);
    }

    @Transactional
    public ItemResponse markFound(Long userId, Long itemId) {
        Item item = findOwnedItem(userId, itemId);
        item.setStatus(Item.Status.found);
        item.setFoundAt(LocalDateTime.now());
        Item saved = itemRepository.save(item);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public byte[] getQrImage(Long userId, Long itemId) {
        Item item = findOwnedItem(userId, itemId);
        try { return qrImageService.generatePng(qrBaseUrl.replaceAll("/+$", "") + "/s/" + item.getQrToken()); }
        catch (Exception ex) { throw new IllegalStateException("Unable to generate QR image.", ex); }
    }

    @Transactional(readOnly = true)
    public PublicItemResponse getPublicItem(String qrToken) {
        if (qrToken == null || !QR_TOKEN.matcher(qrToken).matches()) throw new NotFoundException("Item not found.");
        return itemRepository.findByQrTokenAndDeletedAtIsNull(qrToken).map(PublicItemResponse::from).orElseThrow(() -> new NotFoundException("Item not found."));
    }

    /**
     * Every owner-facing lookup goes through here. Scoping by userId AND
     * deletedAt IS NULL in the same query means an item belonging to someone
     * else, or a soft-deleted item, both come back as a plain 404 — never a
     * 403 that would leak whether the item exists at all.
     */
    private Item findOwnedItem(Long userId, Long itemId) {
        return itemRepository.findByIdAndUserIdAndDeletedAtIsNull(itemId, userId)
                .orElseThrow(() -> new NotFoundException("Item not found."));
    }

    private String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }

    private ItemResponse toResponse(Item item) {
        var last = scanService.findLastForItem(item.getId());
        ItemResponse.LastScanSummary summary = last == null ? null : new ItemResponse.LastScanSummary(last.getScannedAt(), last.getApproxCity(), last.isLocationShared());
        return ItemResponse.from(item, qrBaseUrl, (int) scanService.countForItem(item.getId()), summary);
    }
}
