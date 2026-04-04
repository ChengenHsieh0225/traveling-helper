package com.travelinghelper.planning.domain.event;

import lombok.Builder;

import java.time.LocalTime;

@Builder
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
