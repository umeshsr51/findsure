package com.findsure.controller;

import com.findsure.dto.ItemCreateRequest;
import com.findsure.dto.ItemResponse;
import com.findsure.dto.ItemSummaryResponse;
import com.findsure.dto.ItemUpdateRequest;
import com.findsure.dto.PagedResponse;
import com.findsure.dto.ScanResponse;
import com.findsure.dto.FinderContactResponse;
import com.findsure.entity.Item;
import com.findsure.security.CurrentUser;
import com.findsure.service.ItemService;
import com.findsure.service.ScanService;
import com.findsure.service.FinderContactService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;
    private final ScanService scanService;
    private final FinderContactService finderContactService;

    public ItemController(ItemService itemService, ScanService scanService, FinderContactService finderContactService) {
        this.itemService = itemService;
        this.scanService = scanService;
        this.finderContactService = finderContactService;
    }

    @PostMapping
    public ResponseEntity<ItemResponse> createItem(
            @CurrentUser Long userId,
            @Valid @RequestBody ItemCreateRequest request
    ) {
        ItemResponse response = itemService.createItem(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<ItemSummaryResponse>> getItems(
            @CurrentUser Long userId,
            @RequestParam(required = false) Item.Status status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(itemService.getItems(userId, status, search, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItem(
            @CurrentUser Long userId,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(itemService.getItem(userId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> updateItem(
            @CurrentUser Long userId,
            @PathVariable Long id,
            @Valid @RequestBody ItemUpdateRequest request
    ) {
        return ResponseEntity.ok(itemService.updateItem(userId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(
            @CurrentUser Long userId,
            @PathVariable Long id
    ) {
        itemService.deleteItem(userId, id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/lost")
    public ResponseEntity<ItemResponse> markLost(
            @CurrentUser Long userId,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(itemService.markLost(userId, id));
    }

    @PostMapping("/{id}/found")
    public ResponseEntity<ItemResponse> markFound(
            @CurrentUser Long userId,
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(itemService.markFound(userId, id));
    }

    @GetMapping(value = "/{id}/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQrImage(@CurrentUser Long userId, @PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(itemService.getQrImage(userId, id));
    }

    @GetMapping("/{id}/scans")
    public ResponseEntity<java.util.List<ScanResponse>> getScans(@CurrentUser Long userId, @PathVariable Long id) {
        return ResponseEntity.ok(scanService.getItemScans(userId, id));
    }

    @GetMapping("/{id}/contacts")
    public ResponseEntity<java.util.List<FinderContactResponse>> getContacts(@CurrentUser Long userId, @PathVariable Long id) {
        return ResponseEntity.ok(finderContactService.getItemContacts(userId, id));
    }
}
