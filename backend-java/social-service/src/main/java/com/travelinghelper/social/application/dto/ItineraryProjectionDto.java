package com.travelinghelper.social.application.dto;

import com.travelinghelper.social.domain.model.SharedItineraryType;
import com.travelinghelper.social.domain.model.TimePeriod;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalTime;

public record ItineraryProjectionDto (
    String id,
    @NotBlank(message = "The title of a itinerary projection cannot be empty.")
    String title,
    @NotBlank(message = "The relative date of a itinerary projection cannot be empty.")
    Integer relativeDate,
    String description,
    @NotBlank(message = "The type of a itinerary projection cannot be empty.")
    SharedItineraryType type,
    TimePeriod timePeriod,
    LocalTime startTime,
    LocalTime endTime
) {}
