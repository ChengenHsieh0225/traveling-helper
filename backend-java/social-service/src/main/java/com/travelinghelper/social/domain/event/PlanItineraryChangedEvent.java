package com.travelinghelper.social.domain.event;

import lombok.Builder;

import java.util.List;

@Builder
public record PlanItineraryChangedEvent(
    String id,
    List<EventItemRecord> items
) {}
