package com.travelinghelper.social.application.dto;

import com.travelinghelper.social.domain.model.SharedItineraryType;
import com.travelinghelper.social.domain.model.TimePeriod;
import lombok.Builder;

@Builder
public record SharedPlanItineraryResponse(
    String id,
    String title,
    Integer relativeDate,
    String description,
    SharedItineraryType type,
    TimePeriod timePeriod,
    String durationText
) {}
