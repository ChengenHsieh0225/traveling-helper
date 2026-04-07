package com.travelinghelper.planning.domain.event;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PlanInfoUpdatedEvent (
    String id,
    String title,
    LocalDate startDate,
    LocalDate endDate,
    Integer totalDays,
    String visibility
) {}
