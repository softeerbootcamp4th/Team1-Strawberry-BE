package com.hyundai.softeer.backend.domain.event.service;

import com.hyundai.softeer.backend.domain.car.repository.CarRepository;
import com.hyundai.softeer.backend.domain.event.dto.ApiKeyRequest;
import com.hyundai.softeer.backend.domain.event.dto.EventCreateRequest;
import com.hyundai.softeer.backend.domain.event.dto.EventSimpleDto;
import com.hyundai.softeer.backend.domain.event.dto.UpdateEventPeriodRequest;
import com.hyundai.softeer.backend.domain.event.entity.Event;
import com.hyundai.softeer.backend.domain.event.exception.EventNotFoundException;
import com.hyundai.softeer.backend.domain.event.repository.EventRepository;
import com.hyundai.softeer.backend.infra.s3.service.S3Service;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private static final Logger log = LoggerFactory.getLogger(EventService.class);
    private final EventRepository eventRepository;
    private final CarRepository carRepository;
    private final S3Service s3Service;

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

    public Event createEvent(EventCreateRequest eventCreateRequest) {
        String bannerImgUrl = s3Service.uploadFile(eventCreateRequest.getBannerImg());

        Map<String, Object> images = eventCreateRequest.getImages().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            MultipartFile file = entry.getValue();
                            return s3Service.uploadFile(file);
                        }
                ));

        Event event = Event.builder()
                .car(carRepository.getReferenceById(eventCreateRequest.getCarId()))
                .eventName(eventCreateRequest.getEventName())
                .startAt(eventCreateRequest.getStartAt())
                .endAt(eventCreateRequest.getEndAt())
                .eventStatus(eventCreateRequest.getEventStatus())
                .eventImgUrls(images)
                .build();

        return eventRepository.save(event);
    }

    public void updateEvent(Long eventId, UpdateEventPeriodRequest updateEventPeriodRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException());

        event.updateEventPeriod(updateEventPeriodRequest.getStartAt(), updateEventPeriodRequest.getEndAt());
        eventRepository.save(event);
    }
}
