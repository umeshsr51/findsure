package com.findsure.service;

import com.findsure.dto.LocationShareRequest;
import com.findsure.dto.ScanResponse;
import com.findsure.entity.Item;
import com.findsure.entity.Notification;
import com.findsure.entity.Scan;
import com.findsure.exception.NotFoundException;
import com.findsure.repository.ItemRepository;
import com.findsure.repository.ScanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ScanService {
    private static final Pattern QR_TOKEN = Pattern.compile("FS-[A-HJ-NP-Z2-9]{7}");
    private final ScanRepository scanRepository; private final ItemRepository itemRepository; private final NotificationService notificationService;
    public ScanService(ScanRepository scanRepository, ItemRepository itemRepository, NotificationService notificationService) { this.scanRepository = scanRepository; this.itemRepository = itemRepository; this.notificationService = notificationService; }
    @Transactional public ScanResponse recordScan(String qrToken, String ipAddress, String userAgent) {
        Item item = findPublicItem(qrToken);
        Scan scan = scanRepository.save(Scan.builder().item(item).locationShared(false).ipAddress(truncate(ipAddress, 45)).userAgent(truncate(userAgent, 255)).build());
        notificationService.create(item, scan, Notification.Type.ITEM_SCANNED, "Your item was scanned.");
        return ScanResponse.from(scan);
    }
    @Transactional public ScanResponse shareLocation(Long scanId, LocationShareRequest request) {
        Scan scan = scanRepository.findById(scanId).orElseThrow(() -> new NotFoundException("Scan not found."));
        scan.setLatitude(request.getLatitude()); scan.setLongitude(request.getLongitude()); scan.setApproxCity(truncate(request.getApproxCity(), 120)); scan.setLocationShared(true);
        Scan saved = scanRepository.save(scan);
        notificationService.create(saved.getItem(), saved, Notification.Type.LOCATION_SHARED, "A finder shared the location of a scan.");
        return ScanResponse.from(saved);
    }
    @Transactional(readOnly = true) public List<ScanResponse> getItemScans(Long userId, Long itemId) { ensureOwnedItem(userId, itemId); return scanRepository.findByItemIdOrderByScannedAtDesc(itemId).stream().map(ScanResponse::from).toList(); }
    @Transactional(readOnly = true) public long countForItem(Long itemId) { return scanRepository.countByItemId(itemId); }
    @Transactional(readOnly = true) public Scan findLastForItem(Long itemId) { return scanRepository.findFirstByItemIdOrderByScannedAtDesc(itemId).orElse(null); }
    private Item findPublicItem(String qrToken) { if (qrToken == null || !QR_TOKEN.matcher(qrToken).matches()) throw new NotFoundException("Item not found."); return itemRepository.findByQrTokenAndDeletedAtIsNull(qrToken).orElseThrow(() -> new NotFoundException("Item not found.")); }
    private void ensureOwnedItem(Long userId, Long itemId) { itemRepository.findByIdAndUserIdAndDeletedAtIsNull(itemId, userId).orElseThrow(() -> new NotFoundException("Item not found.")); }
    private String truncate(String value, int max) { return value == null ? null : value.length() <= max ? value : value.substring(0, max); }
}
