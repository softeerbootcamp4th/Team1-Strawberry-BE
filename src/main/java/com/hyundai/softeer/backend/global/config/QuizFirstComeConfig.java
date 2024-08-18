package com.hyundai.softeer.backend.global.config;

import com.hyundai.softeer.backend.domain.firstcome.quiz.service.QuizWinnerDraw;
import com.hyundai.softeer.backend.domain.firstcome.quiz.service.QuizWinnerDrawSync;
import com.hyundai.softeer.backend.domain.subevent.repository.SubEventRepository;
import com.hyundai.softeer.backend.domain.winner.repository.WinnerRepository;
import com.hyundai.softeer.backend.domain.winner.utils.WinnerUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuizFirstComeConfig {

    @Bean
    public QuizWinnerDraw quizWinnerDraw(
            WinnerRepository winnerRepository,
            WinnerUtil winnerUtil
            ) {
       return new QuizWinnerDrawSync(winnerRepository, winnerUtil);
    }
}
