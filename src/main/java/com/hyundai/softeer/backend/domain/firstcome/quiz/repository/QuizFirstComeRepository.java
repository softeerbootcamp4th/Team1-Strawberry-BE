package com.hyundai.softeer.backend.domain.firstcome.quiz.repository;

import com.hyundai.softeer.backend.domain.firstcome.quiz.entity.QuizFirstCome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizFirstComeRepository extends JpaRepository<QuizFirstCome, Long> {
    Optional<QuizFirstCome> findBySubEventId(Long subEventId);

    @Query("SELECT q " +
            "FROM QuizFirstCome q JOIN SubEvent se ON q.subEventId = se.id " +
            "WHERE se.event.id = :eventId AND q.sequence = :sequence")
    Optional<QuizFirstCome> findQuiz(@Param("eventId") Long eventId, @Param("sequence") Integer sequence);

    @Query("UPDATE QuizFirstCome q SET q.winners = q.winners + 1 WHERE q.subEventId = :subEventId")
    @Modifying
    void incrementOneWinners(@Param("subEventId") Long subEventId);

    @Modifying
    @Query(value = "DELETE qe FROM quiz_firstcome_events qe " +
            "INNER JOIN sub_events se ON se.id = qe.sub_event_id " +
            "WHERE se.event_id = :eventId"
            , nativeQuery = true)
    void deleteQuizEventByEventId(@Param("eventId") Long eventId);
}
