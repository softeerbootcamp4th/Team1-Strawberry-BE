package com.hyundai.softeer.backend.domain.car.repository;

import com.hyundai.softeer.backend.domain.car.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
