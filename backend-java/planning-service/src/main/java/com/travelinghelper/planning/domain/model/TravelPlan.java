package com.travelinghelper.planning.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "travel_plans")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TravelPlan {

    @Id
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private String title;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "total_days", nullable = false)
    private Integer totalDays;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "plan_id")
    private List<ItineraryItem> items = new ArrayList<>();

    @Column
    private Visibility visibility = Visibility.PRIVATE;

    private TravelPlan(String userId, String title, LocalDate startDate, LocalDate endDate, Integer totalDays) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalDays = totalDays;
    }

    public static TravelPlan fuzzy(String userId, String title, Integer totalDays) {
        if (totalDays <= 0) {
            throw new IllegalArgumentException("Total days must be positive");
        }
        return new TravelPlan(userId, title, null, null, totalDays);
    }

    public static TravelPlan precise(String userId, String title, LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        int days = (int) ChronoUnit.DAYS.between(start, end) + 1;
        return new TravelPlan(userId, title, start, end, days);
    }

    public void addItemByRelativeDate(String title, ItineraryType type, Integer relativeDate, TimeSlot timeSlot, String description) {
        ItineraryItem newItem = ItineraryItem.createWithRelativeDate(title, type, relativeDate, timeSlot, description);
        validateItemConsistency(newItem);
        this.items.add(newItem);
    }

    private void validateItemConsistency(ItineraryItem item) {
    }

    public void changeVisibility(Visibility newVisibility) {
        this.visibility = newVisibility;
    }

    // Update the inner items
    private ItineraryItem findItemOrThrow(String itemId) {
        return items.stream()
            .filter(i -> i.getId().equals(itemId))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Itinerary item not found: " + itemId));
    }

    public void updateItemRelativeDate(String itemId, Integer newRelativeDate) {
        ItineraryItem item = findItemOrThrow(itemId);
        item.updateRelativeDate(newRelativeDate);
    }

    public void updateItemTitle(String itemId, String newTitle) {
        ItineraryItem item = findItemOrThrow(itemId);
        item.updateTitle(newTitle);
    }

    public void updateItemDescription(String itemId, String newDescription) {
        ItineraryItem item = findItemOrThrow(itemId);
        item.updateDescription(newDescription);
    }

    public void updateItemTimeSlot(String itemId, TimeSlot newTimeSlot) {
        ItineraryItem item = findItemOrThrow(itemId);
        item.updateTimeSlot(newTimeSlot);
    }
}
