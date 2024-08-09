package com.hyundai.softeer.backend.domain.lottery.drawing.service;

import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.DrawingGameInfoDto;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.DrawingInfoDtos;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.DrawingLotteryLandDto;
import com.hyundai.softeer.backend.domain.lottery.drawing.entity.DrawingLotteryEvent;
import com.hyundai.softeer.backend.domain.lottery.drawing.exception.DrawingNotFoundException;
import com.hyundai.softeer.backend.domain.lottery.drawing.repository.DrawingLotteryRepository;
import com.hyundai.softeer.backend.domain.lottery.dto.RankDto;
import com.hyundai.softeer.backend.domain.lottery.service.LotteryService;
import com.hyundai.softeer.backend.domain.subevent.dto.SubEventRequest;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.enums.SubEventType;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
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
    private final DrawingLotteryRepository drawingLotteryRepository;

    @Transactional(readOnly = true)
    public DrawingLotteryLandDto getDrawingLotteryLand(long eventId) {
        List<SubEvent> events = subEventRepository.findByEventId(eventId);
        SubEvent drawing = findDrawingEvent(events);

        if (drawing == null) {
            throw new DrawingNotFoundException();
        }

        return DrawingLotteryLandDto.fromEntity(drawing);
    }

    private SubEvent findDrawingEvent(List<SubEvent> subEvents) {
        for (SubEvent subEvent : subEvents) {
            if (subEvent.getEventType().equals(SubEventType.DRAWING)) {
                return subEvent;
            }
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<RankDto> getRankList(SubEventRequest subEventRequest, int rankCount) {
        Pageable pageable = PageRequest.of(0, rankCount);
        List<RankDto> topNBySubEventId = eventUserRepository.findTopNBySubEventId(subEventRequest.getSubEventId(), pageable);

        for (int i = 0; i < topNBySubEventId.size(); i++) {
            topNBySubEventId.get(i).setRank(i + 1);
        }

        log.info("topNBySubEventId: {}", topNBySubEventId);

        return topNBySubEventId;
    }

    public DrawingInfoDtos getDrawingGameInfo(SubEventRequest subEventRequest) {
        List<DrawingLotteryEvent> drawingEvents = drawingLotteryRepository.findBySubEventId(subEventRequest.getSubEventId());

        if (drawingEvents.isEmpty()) {
            throw new DrawingNotFoundException();
        }

        return new DrawingInfoDtos(drawingEvents.stream()
                .map(DrawingGameInfoDto::fromEntity)
                .toList());
    }
}
