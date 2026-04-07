package com.travelinghelper.planning.domain.event;

import lombok.Builder;

@Builder
public record PlanDeletedEvent (
    String id
) {}
