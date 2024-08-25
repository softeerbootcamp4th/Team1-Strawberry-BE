package com.hyundai.softeer.backend.domain.lottery.drawing.service;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.exception.EventUserNotFoundException;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.lottery.drawing.dto.PreviewRequest;
import com.hyundai.softeer.backend.domain.lottery.drawing.exception.DrawingEventNotParticipantException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
public class PreviewService {

    private final EventUserRepository eventUserRepository;

    public EventUser preview(PreviewRequest previewRequest, Model model) {
        String sharedUrl = previewRequest.getSharedUrl();

        EventUser eventUser = eventUserRepository.findBySharedUrl(sharedUrl)
                .orElseThrow(() -> new EventUserNotFoundException());

        if(eventUser.getResultImgUrl().isEmpty()) {
            throw new DrawingEventNotParticipantException();
        }

        double scoreGame1 = getGameScore(eventUser.getScores().get("1_game_score"));
        model.addAttribute("score", String.format("%.1f", scoreGame1));

        return eventUser;
    }

    private double getGameScore(Object rawGameScore) {
        double scoreGame1;
        if (rawGameScore != null) {
            if (rawGameScore instanceof String) {
                try {
                    scoreGame1 = Double.parseDouble((String) rawGameScore);
                } catch (NumberFormatException e) {
                    scoreGame1 = 0.0;
                }
            } else if (rawGameScore instanceof Number) {
                scoreGame1 = ((Number) rawGameScore).doubleValue();
            } else {
                scoreGame1 = 0.0;
            }
        } else {
            scoreGame1 = 0.0;
        }

        return scoreGame1;
    }
}
