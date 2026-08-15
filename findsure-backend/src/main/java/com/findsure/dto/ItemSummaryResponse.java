package com.findsure.dto;

import com.findsure.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ItemSummaryResponse {

    private Long id;
    private String name;
    private String category;
    private String status;
    private String photoUrl;
    private int scanCount;
    private String qrToken;
    private LocalDateTime createdAt;

    public static ItemSummaryResponse from(Item item) {
        return from(item, 0);
    }

    public static ItemSummaryResponse from(Item item, int scanCount) {
        return new ItemSummaryResponse(
                item.getId(),
                item.getName(),
                item.getCategory(),
                item.getStatus().name(),
                item.getPhotoUrl(),
                scanCount,
                item.getQrToken(),
                item.getCreatedAt()
        );
    }
}
