package com.hyundai.softeer.backend.domain.event.service;

import com.hyundai.softeer.backend.domain.car.repository.CarRepository;
import com.hyundai.softeer.backend.domain.event.dto.ApiKeyRequest;
import com.hyundai.softeer.backend.domain.event.dto.EventSimpleDto;
import com.hyundai.softeer.backend.domain.event.dto.EventUserCountDTO;
import com.hyundai.softeer.backend.domain.event.dto.UpdateEventPeriodRequest;
import com.hyundai.softeer.backend.domain.event.entity.Event;
import com.hyundai.softeer.backend.domain.event.exception.EventNotFoundException;
import com.hyundai.softeer.backend.domain.event.repository.EventRepository;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final CarRepository carRepository;

    @Hidden
    @Value("${api.key}")
    private String apiKeySecret;

    public boolean validateApiKey(ApiKeyRequest apiKeyRequest) {
        return apiKeyRequest.validateApiKey(apiKeySecret);
    }

    public Page<EventSimpleDto> getEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findAll(pageable)
                .map(event -> EventSimpleDto.builder()
                        .id(event.getId())
                        .eventName(event.getEventName())
                        .startAt(event.getStartAt())
                        .endAt(event.getEndAt())
                        .carName(event.getCar().getCarNameKor())
                        .build());
    }

    public void updateEvent(Long eventId, UpdateEventPeriodRequest updateEventPeriodRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException());

        event.updateEventPeriod(updateEventPeriodRequest.getStartAt(), updateEventPeriodRequest.getEndAt());
        eventRepository.save(event);
    }

    public List<EventUserCountDTO> getAnalysis() {
        List<Object[]> totalUsersByEventNative = eventRepository.findTotalUsersByEventNative();
        return EventUserCountDTO.fromObjects(totalUsersByEventNative);
    }
}
