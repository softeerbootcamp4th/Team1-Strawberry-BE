package com.hyundai.softeer.backend.domain.main.service;

import com.hyundai.softeer.backend.domain.event.entity.Event;
import com.hyundai.softeer.backend.domain.event.exception.EventNotFoundException;
import com.hyundai.softeer.backend.domain.event.repository.EventRepository;
import com.hyundai.softeer.backend.domain.main.dto.MainLandDto;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.subevent.exception.SubEventNotFoundException;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.global.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MainService {

    private final EventRepository eventRepository;
    private final SubEventRepository subEventRepository;
    private final DateUtil dateUtil;

    public MainLandDto mainLand(long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException());

        Map<String, Object> eventImgUrls = event.getEventImgUrls();

        List<SubEvent> subEvents = subEventRepository.findByEventId(eventId);

        SubEvent closestSubEvent = dateUtil.findClosestSubEvent(subEvents);

        if(closestSubEvent == null) {
           throw new SubEventNotFoundException();
        }

        int remainSecond = (int) dateUtil.startBetweenCurrentDiff(closestSubEvent);

        return MainLandDto.builder()
                .imgs(eventImgUrls)
                .remainSecond(remainSecond)
                .build();
    }
}
