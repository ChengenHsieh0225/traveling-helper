package com.travelinghelper.social.domain.event;

import com.travelinghelper.social.domain.model.SharedItineraryType;
import com.travelinghelper.social.domain.model.TimePeriod;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalTime;

public record EventItemRecord (
    String id,
    String title,
    Integer relativeDate,
    String description,
    String type,
    String timePeriod,
    LocalTime startTime,
    LocalTime endTime
) {}
