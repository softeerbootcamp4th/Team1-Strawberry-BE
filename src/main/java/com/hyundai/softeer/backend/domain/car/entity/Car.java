package com.hyundai.softeer.backend.domain.car.entity;

import com.hyundai.softeer.backend.domain.car.enums.Brand;
import com.hyundai.softeer.backend.domain.car.enums.BrandConvertor;
import com.hyundai.softeer.backend.domain.car.enums.ModelLine;
import com.hyundai.softeer.backend.domain.car.enums.ModelLineConvertor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "Cars")
public class Car {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Convert(converter = BrandConvertor.class)
  private Brand brandName;

  private String carNameEng;

  private String carNameKor;

  @Convert(converter = ModelLineConvertor.class)
  private ModelLine modelLine;

  private Integer price;

  private LocalDateTime releaseDate;
}
