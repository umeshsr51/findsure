package com.findsure.controller;

import com.findsure.dto.PublicItemResponse;
import com.findsure.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/items")
public class PublicItemController {
    private final ItemService itemService;
    public PublicItemController(ItemService itemService) { this.itemService = itemService; }
    @GetMapping("/{qrToken}") public ResponseEntity<PublicItemResponse> getByQrToken(@PathVariable String qrToken) { return ResponseEntity.ok(itemService.getPublicItem(qrToken)); }
}
