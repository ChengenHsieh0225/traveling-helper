package com.travelinghelper.social.domain.model;

public record ItineraryData (
    String id,
    String title,
    Integer relativeDate,
    String description,
    SharedItineraryType type,
    TimePeriod timePeriod,
    String durationText
) {}
