package com.travelinghelper.social.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shared_plans")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SharedPlan {
    @Id
    private String id; // the id of the original TravelPlan within planning-service

    private String userId;
    private String title;
    private Integer totalDays;

    @OneToMany(mappedBy = "sharedPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SharedItineraryItem> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Visibility visibility;

    // Hold by social-service
    private Integer copyCount = 0;
    private Integer likeCount = 0;

    public static SharedPlan create(String id, String userId, String title, Integer totalDays, Visibility visibility) {
        SharedPlan plan = new SharedPlan();
        plan.id = id;
        plan.userId = userId;
        plan.title = title;
        plan.totalDays = totalDays;
        plan.visibility = visibility;
        return plan;
    }
    // TODO: synchronize shared itinerary items from dtos
    public void syncItinerary() {
    }

    public void incrementCopyCount() {
        this.copyCount++;
    }
    public void incrementLikeCount() {
        this.likeCount++;
    }
    public void decrementLikeCount() {
        this.likeCount--;
    }
}
