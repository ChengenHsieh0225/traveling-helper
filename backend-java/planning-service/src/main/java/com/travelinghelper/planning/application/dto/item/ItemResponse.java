package com.travelinghelper.planning.application.dto.item;

import com.travelinghelper.planning.domain.model.ItineraryType;
import com.travelinghelper.planning.domain.model.TimePeriod;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
public record ItemResponse (
    String id,
    String title,
    ItineraryType type,
    String description,

    LocalDate date,
    Integer relativeDate,

    LocalTime startTime,
    LocalTime endTime,
    TimePeriod timePeriod,

    Boolean isPreciseDate,
    Boolean isPreciseTime
) { }
