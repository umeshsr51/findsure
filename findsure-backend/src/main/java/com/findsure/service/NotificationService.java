package com.findsure.service;

import com.findsure.dto.NotificationResponse;
import com.findsure.entity.Item;
import com.findsure.entity.Notification;
import com.findsure.entity.Scan;
import com.findsure.exception.NotFoundException;
import com.findsure.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    public NotificationService(NotificationRepository notificationRepository) { this.notificationRepository = notificationRepository; }
    @Transactional public void create(Item item, Scan scan, Notification.Type type, String message) { notificationRepository.save(Notification.builder().userId(item.getUserId()).item(item).scan(scan).type(type).message(message).read(false).build()); }
    @Transactional(readOnly = true) public List<NotificationResponse> getNotifications(Long userId) { return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(NotificationResponse::from).toList(); }
    @Transactional public NotificationResponse markRead(Long userId, Long id) { Notification n = notificationRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new NotFoundException("Notification not found.")); n.setRead(true); return NotificationResponse.from(notificationRepository.save(n)); }
}
