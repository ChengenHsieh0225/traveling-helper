package com.travelinghelper.planning.application.dto.plan;

import com.travelinghelper.planning.domain.model.Visibility;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdatePlanRequest {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

    @Min(value = 1, message = "The total days should be larger than 1.")
    private Integer totalDays;

    private Visibility visibility;
}
