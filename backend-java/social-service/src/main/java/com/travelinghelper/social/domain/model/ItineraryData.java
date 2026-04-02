package com.travelinghelper.social.domain.model;

public record ItineraryData (
    String id,
    SharedPlan plan,
    String title,
    Integer relativeDate,
    String description,
    SharedItineraryType type,
    TimePeriod timePeriod,
    String durationText
) {}
