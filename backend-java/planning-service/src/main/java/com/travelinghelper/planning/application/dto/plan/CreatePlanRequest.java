package com.travelinghelper.planning.application.dto.plan;

import com.travelinghelper.planning.domain.model.Visibility;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CreatePlanRequest {
    @NotBlank(message = "The title of a travel plan cannot be empty.")
    @Size(max = 100)
    private String title;

    private LocalDate startDate;
    private LocalDate endDate;

    @Min(value = 1, message = "The total days should be larger than 1.")
    private Integer totalDays;
    private Visibility visibility;
}
