package com.hyundai.softeer.backend.domain.firstcome.quiz.service.winnerdraw;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.firstcome.quiz.dto.QuizFirstComeSubmitResponseDto;
import com.hyundai.softeer.backend.domain.firstcome.quiz.entity.QuizFirstCome;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.user.entity.User;

public interface QuizWinnerDraw {

    QuizFirstComeSubmitResponseDto winnerDraw(EventUser eventUser, QuizFirstCome quizFirstCome, SubEvent subEvent, User authenticatedUser);
}
