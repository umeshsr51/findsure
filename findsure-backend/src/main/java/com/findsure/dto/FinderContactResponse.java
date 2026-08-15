package com.findsure.dto;

import com.findsure.entity.FinderContact;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter @AllArgsConstructor
public class FinderContactResponse {
    private Long id; private Long scanId; private String name; private String email; private String phone; private String message; private LocalDateTime createdAt;
    public static FinderContactResponse from(FinderContact contact) { return new FinderContactResponse(contact.getId(), contact.getScan().getId(), contact.getName(), contact.getEmail(), contact.getPhone(), contact.getMessage(), contact.getCreatedAt()); }
}
