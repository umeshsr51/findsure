package com.findsure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scan_id")
    private Scan scan;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private Type type;
    @Column(nullable = false, length = 500)
    private String message;
    @Column(name = "is_read", nullable = false)
    private boolean read;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @PrePersist protected void onCreate() { if (createdAt == null) createdAt = LocalDateTime.now(); }
    public enum Type { ITEM_SCANNED, LOCATION_SHARED, FINDER_CONTACT }
}
