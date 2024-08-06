package com.hyundai.softeer.backend.domain.expectation.repository;

import com.hyundai.softeer.backend.domain.expectation.entity.Expectation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpectationRepository extends JpaRepository<Expectation, Long> {

    Optional<Expectation> findByEventId(Long eventId);

    Page<Expectation> findByEventId(Long eventId, Pageable pageable);
}
