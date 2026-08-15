package com.findsure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "scans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Scan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(name = "approx_city", length = 120)
    private String approxCity;

    @Column(name = "location_shared", nullable = false)
    private boolean locationShared;

    @Column(name = "scanned_at", nullable = false, updatable = false)
    private LocalDateTime scannedAt;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 255)
    private String userAgent;

    @PrePersist
    protected void onCreate() {
        if (scannedAt == null) scannedAt = LocalDateTime.now();
    }
}
