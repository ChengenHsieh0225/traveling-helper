package com.travelinghelper.social.domain.model;

public enum TimePeriod {
    MORNING, AFTERNOON, EVENING, UNKNOWN;

    public static TimePeriod fromString(String value) {
        try {
            return TimePeriod.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return UNKNOWN;
        }
    }
}
