package com.hyundai.softeer.backend.domain.winner.repository;

import com.hyundai.softeer.backend.domain.winner.entity.Winner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WinnerRepository extends JpaRepository<Winner, Long> {
    Optional<List<Winner>> findBySubEventId(Long subEventId);

    List <Winner> findByUserIdAndSubEventId(Long userId, Long subEventId);

    @Query("SELECT COUNT(w) FROM Winner w WHERE w.subEvent.id = :subEventId")
    long countWinnerBySubEventId(@Param("subEventId") Long subEventId);
}
