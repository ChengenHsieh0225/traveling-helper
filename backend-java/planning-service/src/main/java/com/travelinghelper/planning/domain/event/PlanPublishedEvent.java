package com.travelinghelper.planning.domain.event;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PlanPublishedEvent (
    String id,

    // A null user ID is considered an anonymous share
    String userId,
    String title,
    LocalDate startDate,
    LocalDate endDate,
    Integer totalDays,
    String visibility,
    List<EventItemRecord> items
) {}
