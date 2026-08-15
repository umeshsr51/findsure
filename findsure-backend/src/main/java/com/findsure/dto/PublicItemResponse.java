package com.findsure.dto;

import com.findsure.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PublicItemResponse {
    private String qrToken;
    private String name;
    private String category;
    private String description;
    private String photoUrl;
    private String status;
    private boolean lost;
    public static PublicItemResponse from(Item item) {
        return new PublicItemResponse(item.getQrToken(), item.getName(), item.getCategory(), item.getDescription(), item.getPhotoUrl(), item.getStatus().name(), item.getStatus() == Item.Status.lost);
    }
}
