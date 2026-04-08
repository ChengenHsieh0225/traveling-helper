package com.travelinghelper.social.domain.event;

import java.util.List;

public record PlanPublishedEvent (
    String id,

    // A null user ID is considered an anonymous share
    String userId,
    String title,
    Integer totalDays,
    String visibility,
    List<EventItemRecord> items
) {}
