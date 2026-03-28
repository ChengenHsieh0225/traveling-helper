package com.travelinghelper.planning.application.dto.plan;

import com.travelinghelper.planning.domain.model.Visibility;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;

public record UpdatePlanRequest (
    String title,
    LocalDate startDate,
    LocalDate endDate,

    @Min(value = 1, message = "The total days should be larger than 1.")
    Integer totalDays,

    Visibility visibility
){}
