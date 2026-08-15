package com.findsure.dto;

import com.findsure.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ItemResponse {

    private Long id;
    private String name;
    private String category;
    private String description;
    private String photoUrl;
    private String status;
    private String qrToken;
    private String qrUrl;
    private LocalDateTime lostAt;
    private LocalDateTime foundAt;
    private int scanCount;
    private LastScanSummary lastScan;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Getter
    @AllArgsConstructor
    public static class LastScanSummary {
        private LocalDateTime scannedAt;
        private String approxCity;
        private boolean locationShared;
    }

    public static ItemResponse from(Item item, String qrBaseUrl) {
        return from(item, qrBaseUrl, 0, null);
    }

    public static ItemResponse from(Item item, String qrBaseUrl, int scanCount, LastScanSummary lastScan) {
        return new ItemResponse(
                item.getId(),
                item.getName(),
                item.getCategory(),
                item.getDescription(),
                item.getPhotoUrl(),
                item.getStatus().name(),
                item.getQrToken(),
                qrBaseUrl.replaceAll("/+$", "") + "/s/" + item.getQrToken(),
                item.getLostAt(),
                item.getFoundAt(),
                scanCount,
                lastScan,
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }
}
