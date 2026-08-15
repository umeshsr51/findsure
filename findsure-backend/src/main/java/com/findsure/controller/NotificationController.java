package com.findsure.controller;

import com.findsure.dto.NotificationResponse;
import com.findsure.security.CurrentUser;
import com.findsure.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    public NotificationController(NotificationService notificationService) { this.notificationService = notificationService; }
    @GetMapping public ResponseEntity<List<NotificationResponse>> getNotifications(@CurrentUser Long userId) { return ResponseEntity.ok(notificationService.getNotifications(userId)); }
    @PutMapping("/{id}/read") public ResponseEntity<NotificationResponse> markRead(@CurrentUser Long userId, @PathVariable Long id) { return ResponseEntity.ok(notificationService.markRead(userId, id)); }
}
