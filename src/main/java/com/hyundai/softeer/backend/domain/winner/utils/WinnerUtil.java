package com.hyundai.softeer.backend.domain.winner.utils;

import com.hyundai.softeer.backend.domain.winner.entity.Winner;
import com.hyundai.softeer.backend.domain.winner.repository.WinnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WinnerUtil {

    private final WinnerRepository winnerRepository;

    public List<Winner> isParticipanted(long userId, long subEventId) {
        List<Winner> byUserIdAndSubEventId = winnerRepository.findByUserIdAndSubEventId(userId, subEventId);
        return byUserIdAndSubEventId;
    }
}
