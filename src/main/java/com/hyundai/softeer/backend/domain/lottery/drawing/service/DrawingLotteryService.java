package com.hyundai.softeer.backend.domain.lottery.drawing.service;

import com.hyundai.softeer.backend.domain.eventuser.dto.EventUserInfoDto;
import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.DrawingLotteryLandDto;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.DrawingLotteryLandResponseDto;
import com.hyundai.softeer.backend.domain.lottery.drawing.exception.DrawingNotFoundException;
import com.hyundai.softeer.backend.domain.lottery.dto.RankDto;
import com.hyundai.softeer.backend.domain.lottery.service.LotteryService;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventType;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrawingLotteryService implements LotteryService {
    private final SubEventRepository subEventRepository;
    private final EventUserRepository eventUserRepository;

    @Transactional(readOnly = true)
    public DrawingLotteryLandResponseDto getDrawingLotteryLand(User user, Long eventId) {
        List<SubEvent> events = subEventRepository.findByEventId(eventId);
        SubEvent drawing = findDrawingEvent(events);

        if (drawing == null) {
            throw new DrawingNotFoundException();
        }

        EventUser eventUser = eventUserRepository.findByUserIdAndSubEventId(user.getId(), drawing.getId());

        return DrawingLotteryLandResponseDto.builder()
                .userInfo(EventUserInfoDto.fromEntity(eventUser))
                .eventInfo(DrawingLotteryLandDto.fromEntity(drawing))
                .build();
    }

    private SubEvent findDrawingEvent(List<SubEvent> subEvents) {
        for (SubEvent subEvent : subEvents) {
            if (subEvent.getEventType().equals(SubEventType.DRAWING)) {
                return subEvent;
            }
        }
        return null;
    }

    @Override
    public List<RankDto> getRankList(Long subEventId, int rankCount) {
        Pageable pageable = PageRequest.of(0, rankCount);
        List<RankDto> topNBySubEventId = eventUserRepository.findTopNBySubEventId(subEventId, pageable);

        for (int i = 0; i < topNBySubEventId.size(); i++) {
            topNBySubEventId.get(i).setRank(i + 1);
        }

        log.info("topNBySubEventId: {}", topNBySubEventId);

        return topNBySubEventId;
    }
}
