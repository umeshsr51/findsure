package com.findsure.dto;

import com.findsure.entity.Scan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScanResponse {
    private Long id; private LocalDateTime scannedAt; private String approxCity; private boolean locationShared; private Double latitude; private Double longitude;
    public static ScanResponse from(Scan scan) { return new ScanResponse(scan.getId(), scan.getScannedAt(), scan.getApproxCity(), scan.isLocationShared(), scan.getLatitude(), scan.getLongitude()); }
}
