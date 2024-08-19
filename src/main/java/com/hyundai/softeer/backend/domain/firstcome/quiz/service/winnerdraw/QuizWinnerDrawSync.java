package com.hyundai.softeer.backend.domain.firstcome.quiz.service.winnerdraw;

import com.hyundai.softeer.backend.domain.eventuser.entity.EventUser;
import com.hyundai.softeer.backend.domain.eventuser.repository.EventUserRepository;
import com.hyundai.softeer.backend.domain.firstcome.quiz.dto.QuizFirstComeSubmitResponseDto;
import com.hyundai.softeer.backend.domain.firstcome.quiz.entity.QuizFirstCome;
import com.hyundai.softeer.backend.domain.firstcome.quiz.exception.AlreadyWonEventUserException;
import com.hyundai.softeer.backend.domain.firstcome.quiz.repository.QuizFirstComeRepository;
import com.hyundai.softeer.backend.domain.firstcome.quiz.service.winnerdraw.QuizWinnerDraw;
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
public class QuizWinnerDrawSync implements QuizWinnerDraw {

    private final WinnerRepository winnerRepository;
    private final WinnerUtil winnerUtil;
    private final QuizFirstComeRepository quizFirstComeRepository;
    private final EventUserRepository eventUserRepository;

    @Override
    public synchronized QuizFirstComeSubmitResponseDto winnerDraw(
            EventUser eventUser,
            QuizFirstCome quizFirstCome,
            SubEvent subEvent,
            User authenticatedUser) {

        if (!winnerUtil.isParticipanted(authenticatedUser.getId(), subEvent.getId()).isEmpty()) {
            throw new AlreadyWonEventUserException();
        }

        int winners = quizFirstCome.getWinners();
        int winnerCount = (int) winnerRepository.countWinnerBySubEventId(subEvent.getId());

        if (winnerCount >= winners) {
            return QuizFirstComeSubmitResponseDto.correctBut();
        }

        quizFirstCome.setWinnerCount(winnerCount + 1);
        quizFirstComeRepository.flush();


        Prize prize = quizFirstCome.getPrize();

        Winner winner = new Winner();
        winner.setPrize(prize);
        winner.setSubEvent(subEvent);
        winner.setUser(authenticatedUser);
        winnerRepository.save(winner);

        eventUser.updateWinner();
        eventUserRepository.save(eventUser);

        return QuizFirstComeSubmitResponseDto.winner(prize.getPrizeWinningImgUrl());
    }
}
