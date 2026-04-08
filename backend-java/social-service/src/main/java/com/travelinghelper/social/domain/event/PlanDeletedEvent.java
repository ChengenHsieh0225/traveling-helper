package com.travelinghelper.social.domain.event;

import lombok.Builder;

@Builder
public record PlanDeletedEvent (
    String id
) {}
