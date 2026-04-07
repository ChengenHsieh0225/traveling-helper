package com.travelinghelper.social.application.dto;

import com.travelinghelper.social.domain.model.SocialVisibility;
import lombok.Builder;

@Builder
public record SharedPlanSummaryResponse(
   String id,
   String userId,
   String title,
   Integer totalDays,
   SocialVisibility visibility,
   Integer copyCount,
   Integer likeCount
) {}
