package com.hyundai.softeer.backend.domain.winner.repository;

import com.hyundai.softeer.backend.domain.winner.entity.Winner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WinnerRepository extends JpaRepository<Winner, Long> {
}
