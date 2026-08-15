package com.findsure.service;

import com.findsure.dto.FinderContactRequest;
import com.findsure.dto.FinderContactResponse;
import com.findsure.entity.FinderContact;
import com.findsure.entity.Notification;
import com.findsure.entity.Scan;
import com.findsure.exception.NotFoundException;
import com.findsure.repository.FinderContactRepository;
import com.findsure.repository.ItemRepository;
import com.findsure.repository.ScanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class FinderContactService {
    private final FinderContactRepository contactRepository; private final ScanRepository scanRepository; private final ItemRepository itemRepository; private final NotificationService notificationService;
    public FinderContactService(FinderContactRepository contactRepository, ScanRepository scanRepository, ItemRepository itemRepository, NotificationService notificationService) { this.contactRepository = contactRepository; this.scanRepository = scanRepository; this.itemRepository = itemRepository; this.notificationService = notificationService; }
    @Transactional public FinderContactResponse create(Long scanId, FinderContactRequest request) {
        Scan scan = scanRepository.findById(scanId).orElseThrow(() -> new NotFoundException("Scan not found."));
        FinderContact saved = contactRepository.save(FinderContact.builder().scan(scan).name(trim(request.getName())).email(trim(request.getEmail())).phone(trim(request.getPhone())).message(request.getMessage().trim()).build());
        notificationService.create(scan.getItem(), scan, Notification.Type.FINDER_CONTACT, "A finder sent you a contact message.");
        return FinderContactResponse.from(saved);
    }
    @Transactional(readOnly = true) public List<FinderContactResponse> getItemContacts(Long userId, Long itemId) { itemRepository.findByIdAndUserIdAndDeletedAtIsNull(itemId, userId).orElseThrow(() -> new NotFoundException("Item not found.")); return contactRepository.findByScanItemIdOrderByCreatedAtDesc(itemId).stream().map(FinderContactResponse::from).toList(); }
    private String trim(String value) { return value == null ? null : value.trim(); }
}
