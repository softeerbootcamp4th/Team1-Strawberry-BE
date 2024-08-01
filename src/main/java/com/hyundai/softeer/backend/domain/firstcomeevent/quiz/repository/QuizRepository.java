package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.repository;

import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository ;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findBySubEventId(Long subEventId);

    @Query("SELECT q " +
            "FROM Quiz q JOIN SubEvent se ON q.subEventId = se.id " +
            "WHERE se.event.id = :eventId AND q.sequence = :sequence")
    Optional<Quiz> findQuiz(@Param("eventId") Long eventId, @Param("sequence") Integer sequence);

    @Query("UPDATE Quiz q SET q.winners = q.winners + 1 WHERE q.subEventId = :subEventId")
    @Modifying
    void incrementOneWinners(@Param("subEventId") Long subEventId);
}
