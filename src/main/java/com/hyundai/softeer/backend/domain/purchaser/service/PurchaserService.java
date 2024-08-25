package com.hyundai.softeer.backend.domain.purchaser.service;

import com.hyundai.softeer.backend.domain.purchaser.dto.EventPurchaserCount;
import com.hyundai.softeer.backend.domain.purchaser.dto.PurchaserDto;
import com.hyundai.softeer.backend.domain.purchaser.repository.PurchaserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaserService {
    private final PurchaserRepository purchaserRepository;

    public Page<PurchaserDto> getPurchasers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return purchaserRepository.findAll(pageable)
                .map(purchaser -> PurchaserDto.builder()
                        .id(purchaser.getId())
                        .userId(purchaser.getUser().getId())
                        .carName(purchaser.getCar().getCarNameKor())
                        .build());
    }

    public List<EventPurchaserCount> getAnalysis() {
        return purchaserRepository.countPurchasersGroupedByEventId();
    }
}
