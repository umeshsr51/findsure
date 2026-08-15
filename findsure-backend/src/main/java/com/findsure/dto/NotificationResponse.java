package com.findsure.dto;

import com.findsure.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter @AllArgsConstructor
public class NotificationResponse {
    private Long id; private Long itemId; private Long scanId; private String type; private String message; private boolean read; private LocalDateTime createdAt;
    public static NotificationResponse from(Notification n) { return new NotificationResponse(n.getId(), n.getItem() == null ? null : n.getItem().getId(), n.getScan() == null ? null : n.getScan().getId(), n.getType().name(), n.getMessage(), n.isRead(), n.getCreatedAt()); }
}
