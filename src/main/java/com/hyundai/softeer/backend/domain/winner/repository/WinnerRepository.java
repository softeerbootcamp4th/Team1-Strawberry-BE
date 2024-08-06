package com.hyundai.softeer.backend.domain.winner.repository;

import com.hyundai.softeer.backend.domain.winner.entity.Winner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WinnerRepository extends JpaRepository<Winner, Long> {
    Optional<List<Winner>> findBySubEventId(Long subEventId);
}
