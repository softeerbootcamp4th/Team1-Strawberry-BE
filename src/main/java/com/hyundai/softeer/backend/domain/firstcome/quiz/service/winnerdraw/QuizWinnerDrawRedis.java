package com.hyundai.softeer.backend.domain.firstcome.quiz.service.winnerdraw;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.firstcome.quiz.dto.QuizFirstComeSubmitResponseDto;
import com.hyundai.softeer.backend.domain.firstcome.quiz.entity.QuizFirstCome;
import com.hyundai.softeer.backend.domain.firstcome.quiz.service.CounterService;
import com.hyundai.softeer.backend.domain.prize.entity.Prize;
import com.hyundai.softeer.backend.domain.subevent.entity.SubEvent;
import com.hyundai.softeer.backend.domain.user.entity.User;
import com.hyundai.softeer.backend.domain.winner.entity.Winner;
import com.hyundai.softeer.backend.domain.winner.repository.WinnerRepository;
import com.hyundai.softeer.backend.domain.winner.utils.WinnerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizWinnerDrawRedis implements QuizWinnerDraw {

    private final WinnerUtil winnerUtil;
    private final WinnerRepository winnerRepository;
    private final CounterService counterService;

    @Override
    public synchronized QuizFirstComeSubmitResponseDto winnerDraw(EventUser eventUser, QuizFirstCome quizFirstCome, SubEvent subEvent, User authenticatedUser) {
        if (!winnerUtil.isParticipanted(authenticatedUser.getId(), subEvent.getId()).isEmpty()) {
            return QuizFirstComeSubmitResponseDto.alreadyParticipant();
        }

        long winners = quizFirstCome.getWinners();
        long winnerCount = counterService.getCounterValue(CounterService.COUNTER_KEY);

        if (winnerCount >= winners) {
            return QuizFirstComeSubmitResponseDto.correctBut();
        }

        Prize prize = quizFirstCome.getPrize();

        Winner winner = new Winner();
        winner.setPrize(prize);
        winner.setSubEvent(subEvent);
        winner.setUser(authenticatedUser);
        winnerRepository.save(winner);

        counterService.incrementCounter(CounterService.COUNTER_KEY);

        return QuizFirstComeSubmitResponseDto.winner(prize.getPrizeWinningImgUrl());
    }
}
