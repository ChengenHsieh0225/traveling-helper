package com.travelinghelper.social.application.dto;

import com.travelinghelper.social.domain.model.Visibility;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record PlanProjectionDto (
    @NotBlank(message = "The id of a plan projection cannot be empty.")
    String id,

    // A null user ID is considered an anonymous share
    String userId,
    String title,
    Integer totalDays,
    Visibility visibility,
    List<ItineraryProjectionDto> items
) {}
