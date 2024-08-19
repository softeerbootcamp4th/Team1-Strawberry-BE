package com.hyundai.softeer.backend.domain.lottery.drawing.service.rank;

import com.hyundai.softeer.backend.domain.lottery.drawing.dto.DrawingTotalScoreDto;
import com.hyundai.softeer.backend.domain.lottery.dto.RankDto;
import com.hyundai.softeer.backend.domain.subevent.dto.SubEventRequest;
import com.hyundai.softeer.backend.domain.user.entity.User;

import java.util.List;

public interface DrawingRank {
    List<RankDto> getRankList(SubEventRequest subEventRequest, int rankCount);

    DrawingTotalScoreDto getDrawingTotalScore(User authenticatedUser, SubEventRequest subEventRequest);

    void updateRankingData(long subEventId, int rankCount);
}
