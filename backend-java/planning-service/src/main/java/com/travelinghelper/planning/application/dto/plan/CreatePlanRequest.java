package com.travelinghelper.planning.application.dto.plan;

import com.travelinghelper.planning.domain.model.Visibility;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreatePlanRequest (
    @NotBlank(message = "The title of a travel plan cannot be empty.")
    @Size(max = 100)
    String title,

    LocalDate startDate,
    LocalDate endDate,

    @Min(value = 1, message = "The total days should be larger than 1.")
    Integer totalDays,
    Visibility visibility
) {}
