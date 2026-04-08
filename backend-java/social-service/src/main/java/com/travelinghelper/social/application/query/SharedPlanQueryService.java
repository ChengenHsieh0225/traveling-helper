package com.travelinghelper.social.application.query;

import com.travelinghelper.social.application.dto.SharedPlanItineraryResponse;
import com.travelinghelper.social.application.dto.SharedPlanSummaryResponse;
import com.travelinghelper.social.domain.model.SharedItineraryType;
import com.travelinghelper.social.domain.model.SocialVisibility;
import com.travelinghelper.social.domain.model.TimePeriod;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SharedPlanQueryService {

    private final JdbcClient jdbcClient;

    @Transactional(readOnly = true)
    public List<SharedPlanSummaryResponse> listSharedPlans(String userId) {
        String sql = """
            SELECT id, user_id, title, total_days, visibility, copy_count, like_count
            FROM shared_plans
            WHERE visibility = 'PUBLIC' OR user_id = :userId
            """;

        return jdbcClient.sql(sql)
            .param("userId", userId)
            .query((rs, rowNum) -> SharedPlanSummaryResponse.builder()
                .id(rs.getString("id"))
                .userId(rs.getString("user_id"))
                .title(rs.getString("title"))
                .totalDays(rs.getInt("total_days"))
                .visibility(SocialVisibility.fromString(rs.getString("visibility")))
                .copyCount(rs.getInt("copy_count"))
                .likeCount(rs.getInt("like_count"))
                .build())
            .list();
    }

    @Transactional(readOnly = true)
    public List<SharedPlanItineraryResponse> getItineraryListFromPlan(String planId) {
        String sql = """
            SELECT id, title, relative_date, description, type, time_period, duration_text
            FROM shared_itinerary_items
            WHERE shared_plan_id = :planId
            """;
        return jdbcClient.sql(sql)
            .param("planId", planId)
            .query((rs, rowNum) -> SharedPlanItineraryResponse.builder()
                .id(rs.getString("id"))
                .title(rs.getString("title"))
                .relativeDate(rs.getInt("relative_date"))
                .description(rs.getString("description"))
                .type(SharedItineraryType.fromString(rs.getString("type")))
                .timePeriod(TimePeriod.fromString(rs.getString("time_period")))
                .durationText(rs.getString("duration_text"))
                .build())
            .list();
    }
}
