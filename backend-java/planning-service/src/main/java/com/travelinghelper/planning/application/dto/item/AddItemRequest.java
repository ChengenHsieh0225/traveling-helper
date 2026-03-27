package com.travelinghelper.planning.application.dto.item;

import com.travelinghelper.planning.domain.model.ItineraryType;
import com.travelinghelper.planning.domain.model.TimePeriod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class AddItemRequest {
    @NotBlank private String title;
    @NotNull private ItineraryType type;
    private String description;

    private Integer relativeDate;

    private LocalTime startTime;
    private LocalTime endTime;
    private TimePeriod timePeriod;
}
