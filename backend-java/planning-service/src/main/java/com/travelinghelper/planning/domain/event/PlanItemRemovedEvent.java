package com.travelinghelper.planning.domain.event;

import lombok.Builder;

@Builder
public record PlanItemRemovedEvent(
    String planId,
    String itemId
) {}
