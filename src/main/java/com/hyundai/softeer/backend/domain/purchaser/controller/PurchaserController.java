package com.hyundai.softeer.backend.domain.purchaser.controller;

import com.hyundai.softeer.backend.domain.purchaser.dto.EventPurchaserCount;
import com.hyundai.softeer.backend.domain.purchaser.dto.PurchaserDto;
import com.hyundai.softeer.backend.domain.purchaser.service.PurchaserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchaser")
@RequiredArgsConstructor
public class PurchaserController {
    private final PurchaserService purchaserService;

    @GetMapping("/list")
    public Page<PurchaserDto> getPurchasers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return purchaserService.getPurchasers(page, size);
    }

    @GetMapping("/analysis")
    public List<EventPurchaserCount> getAnalysis() {
        return purchaserService.getAnalysis();
    }
}
