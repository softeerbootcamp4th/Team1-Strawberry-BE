package com.hyundai.softeer.backend.domain.event.controller;

import com.hyundai.softeer.backend.domain.event.dto.EventCreateRequest;
import com.hyundai.softeer.backend.domain.event.dto.EventSimpleDto;
import com.hyundai.softeer.backend.domain.event.dto.UpdateEventPeriodRequest;
import com.hyundai.softeer.backend.domain.event.service.EventService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Admin")
@RequestMapping("/api/v1/event")
public class EventController {
    private final EventService eventService;

    @GetMapping("/list")
    public Page<EventSimpleDto> getEvents(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return eventService.getEvents(page, size);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createEvent(@RequestBody EventCreateRequest eventCreateRequest) {
        eventService.createEvent(eventCreateRequest);
    }

    @PutMapping("/{eventId}")
    public void updateEventPeriod(
            @PathVariable Long eventId,
            @RequestBody UpdateEventPeriodRequest updateEventPeriodRequest) {
        eventService.updateEvent(eventId, updateEventPeriodRequest);
    }
}
