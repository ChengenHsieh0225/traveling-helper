package com.travelinghelper.planning.application.dto.plan;

import com.travelinghelper.planning.application.dto.item.ItemResponse;
import com.travelinghelper.planning.domain.model.Visibility;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PlanResponse {
    private String id;
    private String userId;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalDays;
    private Visibility visibility;

    private List<ItemResponse> items;

    private Boolean isPrecise;
}
