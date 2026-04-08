package com.travelinghelper.planning.application.dto.error;

public record ErrorResponse(
    int status,
    String error,
    String message,
    long timestamp
) {}