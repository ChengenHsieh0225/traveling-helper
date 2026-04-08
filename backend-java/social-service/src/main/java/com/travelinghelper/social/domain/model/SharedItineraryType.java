package com.travelinghelper.social.domain.model;

public enum SharedItineraryType {
    TRANSPORT, ACCOMMODATION, SIGHTSEEING, FOOD, UNKNOWN;

    public static SharedItineraryType fromString(String value) {
        try {
            return SharedItineraryType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return UNKNOWN;
        }
    }
}
