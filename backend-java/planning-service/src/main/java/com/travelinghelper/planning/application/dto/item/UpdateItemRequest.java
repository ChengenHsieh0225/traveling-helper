package com.travelinghelper.planning.application.dto.item;

import com.travelinghelper.planning.domain.model.ItineraryType;
import com.travelinghelper.planning.domain.model.TimePeriod;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class UpdateItemRequest {
    private String title;
    private ItineraryType type;
    private String description;

    private Integer relativeDate;

    private LocalTime startTime;
    private LocalTime endTime;
    private TimePeriod timePeriod;
}
