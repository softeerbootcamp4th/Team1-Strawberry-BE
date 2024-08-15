package com.hyundai.softeer.backend.domain.event.controller;

import com.hyundai.softeer.backend.domain.event.dto.EventSimpleDto;
import com.hyundai.softeer.backend.domain.event.service.EventService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
