package com.travelinghelper.planning.domain.event;

import com.travelinghelper.planning.application.event.ApplicationEvent;
import lombok.Builder;

@Builder
public record PlanDeletedEvent (
    String id
) implements ApplicationEvent {}
