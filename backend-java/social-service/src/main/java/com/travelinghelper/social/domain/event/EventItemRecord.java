package com.travelinghelper.social.domain.event;

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
