package com.travelinghelper.planning.application.dto.item;

import com.travelinghelper.planning.domain.model.ItineraryType;
import com.travelinghelper.planning.domain.model.TimePeriod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record AddItemRequest (
    @NotBlank String title,
    @NotNull ItineraryType type,
    String description,

    Integer relativeDate,

    LocalTime startTime,
    LocalTime endTime,
    TimePeriod timePeriod
) {}
