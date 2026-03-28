package com.travelinghelper.planning.application.dto.plan;

import com.travelinghelper.planning.application.dto.item.ItemResponse;
import com.travelinghelper.planning.domain.model.Visibility;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PlanResponse (
    String id,
    String userId,
    String title,
    LocalDate startDate,
    LocalDate endDate,
    Integer totalDays,
    Visibility visibility,

    List<ItemResponse> items,

    Boolean isPrecise
) {}
