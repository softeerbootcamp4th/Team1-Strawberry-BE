package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.repository;

import com.hyundai.softeer.backend.domain.firstcomeevent.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Quiz findBySubEventIdAndSequence(Long subEventId, Integer sequence);

    Quiz findBySubEventId(Long subEventId);
}
