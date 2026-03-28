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

    @Column(name = "user_id", nullable = false, updatable = false)
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

    // Constructors and factory methods
    private TravelPlan(String userId, String title, LocalDate startDate, LocalDate endDate, Integer totalDays) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("UserId is required");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

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

    // State methods for asking
    public boolean isPrecise() {
        return this.startDate != null && this.endDate != null;
    }

    // Update methods
    public void updateTitle(String newTitle) {
        if (newTitle == null || newTitle.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = newTitle;
    }

    public void updatePreciseDates(LocalDate newStart, LocalDate newEnd) {
        if (newStart == null || newEnd == null) {
            throw new IllegalArgumentException("Both start and end dates must be provided for precise plans");
        }
        if (newStart.isAfter(newEnd)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }

        this.startDate = newStart;
        this.endDate = newEnd;

        int newTotalDays = (int) ChronoUnit.DAYS.between(newStart, newEnd) + 1;

        applyNewTotalDays(newTotalDays);
    }

    public void updateTotalDays(Integer newTotalDays) {
        if (newTotalDays == null || newTotalDays <= 0) {
            throw new IllegalArgumentException("Total days must be a positive integer");
        }
        if (this.startDate != null) {
            this.endDate = this.startDate.plusDays(newTotalDays - 1);
        }
        applyNewTotalDays(newTotalDays);
    }

    public void changeVisibility(Visibility newVisibility) {
        if (newVisibility == null) {
            throw new IllegalArgumentException("Visibility cannot be null");
        }
        this.visibility = newVisibility;
    }

    private void applyNewTotalDays(Integer newTotalDays) {
        boolean hasInvalidItems = this.items.stream()
            .anyMatch(item -> item.getRelativeDate() > newTotalDays);

        if (hasInvalidItems) {
            throw new IllegalStateException(
                "Cannot shorten plan duration: Some itinerary items are scheduled beyond the new total days."
            );
        }

        this.totalDays = newTotalDays;
    }

    // Create and update ItineraryItem
    public void addItemByRelativeDate(String title, ItineraryType type, Integer relativeDate, TimeSlot timeSlot, String description) {
        ItineraryItem newItem = ItineraryItem.createWithRelativeDate(title, type, relativeDate, timeSlot, description);
        validateItemConsistency(newItem);
        this.items.add(newItem);
    }

    private void validateItemConsistency(ItineraryItem item) {
        if (item.getRelativeDate() > this.totalDays) {
            throw new IllegalArgumentException(
                String.format("Relative date %d exceeds total days %d",
                    item.getRelativeDate(), this.totalDays)
            );
        }
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
        if (newRelativeDate > this.totalDays) {
            throw new IllegalArgumentException("The relative date exceeds plan duration");
        }
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

    public void removeItem(String itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("Item ID cannot be null");
        }
        boolean removed = this.items.removeIf(item -> item.getId().equals(itemId));
        if (!removed) {
            throw new IllegalArgumentException("Itinerary item not found: " + itemId);
        }
    }
}
