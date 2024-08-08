package com.hyundai.softeer.backend.domain.lottery.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@MappedSuperclass
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
public class LotteryEvent {
    @Id
    @Column(name = "sub_event_id", nullable = false)
    private Long subEventId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private Map<String, Object> winnersMeta;
}
