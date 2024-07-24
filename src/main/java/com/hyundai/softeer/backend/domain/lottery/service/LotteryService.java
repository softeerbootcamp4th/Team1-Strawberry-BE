package com.hyundai.softeer.backend.domain.lottery.service;

import com.hyundai.softeer.backend.domain.lottery.repository.LotteryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LotteryService {
    private final LotteryRepository lotteryRepository;

}
