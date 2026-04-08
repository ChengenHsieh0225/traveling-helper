package com.travelinghelper.social.domain.event;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PlanHeaderUpdatedEvent(
    String id,
    String title,
    LocalDate startDate,
    LocalDate endDate,
    Integer totalDays,
    String visibility
) {}
