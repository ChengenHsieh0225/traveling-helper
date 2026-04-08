package com.travelinghelper.social.domain.model;

public enum SocialVisibility {
    PUBLIC, FRIEND, PRIVATE, UNKNOWN;

    public static SocialVisibility fromString(String value) {
        try {
            return SocialVisibility.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        } catch (NullPointerException e) {
            return PRIVATE;
        }
    }
}
