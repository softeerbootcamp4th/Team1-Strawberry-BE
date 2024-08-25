package com.hyundai.softeer.backend.domain.subevent.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateWinnerMetaRequest {
    private int rank;

    private int amount;

    private int prizeId;
}
