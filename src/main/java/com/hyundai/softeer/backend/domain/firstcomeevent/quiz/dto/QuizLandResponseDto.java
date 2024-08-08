package com.hyundai.softeer.backend.domain.firstcomeevent.quiz.dto;

import com.hyundai.softeer.backend.domain.prize.entity.Prize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Builder
@Getter
public class QuizLandResponseDto {

    @Schema(description = "이벤트 유효 여부", example = "true")
    private boolean valid;

    @Schema(description = "배너 이미지 Url", example = "https://www.awss3/bannerImage")
    private String bannerImg;

    @Schema(description = "이벤트 이미지 Url", example = "https://www.awss3/eventImg")
    private Map<String, Object> eventImg;

    @Schema(description = "이벤트까지 남은 시간", example = "3959")
    private long remainSecond;

    @Schema(description = "퀴즈 개요", example = "디 올 뉴 산타페 하이브리드의")
    private String overview;

    @Schema(description = "퀴즈 문제", example = "산타페의 연비는?")
    private String problem;

    @Schema(description = "퀴즈 마지막 번호", example = "3")
    private int lastQuizNumber;

    @Schema(description = "퀴즈 힌트", example = "10.x 입니다.")
    private String hint;

    @Schema(description = "html anchor", example = "???")
    private String anchor;

    @Schema(description = "서브 이벤트 id", example = "1")
    private long subEventId;

    @Schema(description = "선착순 경품 정보들이 담겨있음", example = "PrizeInfo Dto 배열 들어가요. PrizeInfo Dto 확인해주세요 🥹")
    private List<PrizeInfo> prizeInfos;

    @Schema(description = "시작 시간", example = "2024-06-25 10:30:00")
    private LocalDateTime startAt;

    @Schema(description = "마감 시간", example = "2024-06-26 10:30:00")
    private LocalDateTime endAt;

    @Schema(description = "당첨자 수", example = "100")
    private int winners;


    public static QuizLandResponseDto eventNotValid() {
       return QuizLandResponseDto.builder()
               .valid(false)
               .build();
    }
}
