package com.hyundai.softeer.backend.domain.prize.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Prizes")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Prize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private Integer price;

    private String prizeImgUrl;

    private String prizeWinningImgUrl;
}
