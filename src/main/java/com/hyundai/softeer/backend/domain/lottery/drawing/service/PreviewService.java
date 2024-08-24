package com.hyundai.softeer.backend.domain.lottery.drawing.service;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.exception.EventUserNotFoundException;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.PreviewRequest;
import com.hyundai.softeer.backend.domain.lottery.drawing.exception.DrawingEventNotParticipantException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreviewService {

    private final EventUserRepository eventUserRepository;

    public EventUser preview(PreviewRequest previewRequest) {
        String sharedUrl = previewRequest.getSharedUrl();

        EventUser eventUser = eventUserRepository.findBySharedUrl(sharedUrl)
                .orElseThrow(() -> new EventUserNotFoundException());

        if(eventUser.getResultImgUrl().isEmpty()) {
            throw new DrawingEventNotParticipantException();
        }

        return eventUser;
    }
}
