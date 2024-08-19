package com.hyundai.softeer.backend.domain.firstcome.quiz.service;

import com.hyundai.softeer.backend.domain.firstcome.quiz.dto.QuizFirstComeSubmitResponseDto;
import com.hyundai.softeer.backend.domain.firstcome.quiz.entity.QuizFirstCome;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.user.entity.User;

public interface QuizWinnerDraw {

    QuizFirstComeSubmitResponseDto winnerDraw(QuizFirstCome quizFirstCome, SubEvent subEvent, User authenticatedUser);
}
