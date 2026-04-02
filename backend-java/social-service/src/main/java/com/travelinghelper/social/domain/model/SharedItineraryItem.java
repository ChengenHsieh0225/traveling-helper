package com.travelinghelper.social.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "social_itinerary_items")
@Getter
@NoArgsConstructor
public class SharedItineraryItem {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shared_plan_id")
    private SharedPlan sharedPlan;

    private String title;
    private Integer relativeDate;
    private String description;

    @Enumerated(EnumType.STRING)
    private SharedItineraryType type;

    private TimePeriod timePeriod;
    private String durationText; // eg., 2h 30m, 45m

    SharedItineraryItem(String id, SharedPlan plan, String title, Integer relativeDate, String description, SharedItineraryType type, TimePeriod timePeriod, String durationText) {
        this.id = id;
        this.sharedPlan = plan;
        this.title = title;
        this.relativeDate = relativeDate;
        this.description = description;
        this.type = type;
        this.timePeriod = timePeriod;
        this.durationText = durationText;
    }
}
