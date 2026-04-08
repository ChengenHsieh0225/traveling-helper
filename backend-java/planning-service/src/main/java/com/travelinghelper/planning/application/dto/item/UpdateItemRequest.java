package com.travelinghelper.planning.application.dto.item;

import com.travelinghelper.planning.domain.model.TimePeriod;

import java.time.LocalTime;

public record UpdateItemRequest (
    String title,
    String description,

    Integer relativeDate,

    LocalTime startTime,
    LocalTime endTime,
    TimePeriod timePeriod
) {}
