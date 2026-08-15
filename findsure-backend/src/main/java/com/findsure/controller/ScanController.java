package com.findsure.controller;

import com.findsure.dto.*;
import com.findsure.service.FinderContactService;
import com.findsure.service.ScanService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/scan")
public class ScanController {
    private final ScanService scanService; private final FinderContactService contactService;
    public ScanController(ScanService scanService, FinderContactService contactService) { this.scanService = scanService; this.contactService = contactService; }
    @PostMapping("/{qrToken}") public ResponseEntity<ScanResponse> record(@PathVariable String qrToken, HttpServletRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(scanService.recordScan(qrToken, clientIp(request), request.getHeader("User-Agent"))); }
    @PostMapping("/{scanId}/location") public ResponseEntity<ScanResponse> shareLocation(@PathVariable Long scanId, @Valid @RequestBody LocationShareRequest request) { return ResponseEntity.ok(scanService.shareLocation(scanId, request)); }
    @PostMapping("/{scanId}/contact") public ResponseEntity<FinderContactResponse> contact(@PathVariable Long scanId, @Valid @RequestBody FinderContactRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(contactService.create(scanId, request)); }
    private String clientIp(HttpServletRequest request) { String forwarded = request.getHeader("X-Forwarded-For"); return forwarded == null || forwarded.isBlank() ? request.getRemoteAddr() : forwarded.split(",")[0].trim(); }
}
