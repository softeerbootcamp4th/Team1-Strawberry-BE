package com.hyundai.softeer.backend.domain.car;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "car")
public class Car {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long carId;

  @Convert(converter = BrandConvertor.class)
  private Brand brandName;

  private String carNameEng;
  private String carNameKor;
}
