package com.travelinghelper.planning.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "itinerary_items")
@Getter
@NoArgsConstructor
public class ItineraryItem {

    @Id
    private String id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private ItineraryType type;

    @Column(name = "relative_date", nullable = false)
    private Integer relativeDate;

    @Embedded
    private TimeSlot timeSlot;

    @Column
    private String description;

    ItineraryItem(String title, ItineraryType type, Integer relativeDate, TimeSlot timeSlot, String description) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (type == null) {
            throw new IllegalArgumentException("Itinerary Type is required");
        }
        if (relativeDate == null || relativeDate < 1) {
            throw new IllegalArgumentException("RelativeDate must be at least 1");
        }

        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.type = type;
        this.relativeDate = relativeDate;
        this.timeSlot = timeSlot;
        this.description = description;
    }

    static ItineraryItem createWithRelativeDate(String title, ItineraryType type, Integer relativeDate, TimeSlot timeSlot, String description) {
        return new ItineraryItem(title, type, relativeDate, timeSlot, description);
    }

    void updateTimeSlot(TimeSlot newTimeSlot) {
        if (newTimeSlot == null) {
            throw new IllegalArgumentException("TimeSlot cannot be null");
        }
        this.timeSlot = newTimeSlot;
    }

    void updateTitle(String newTitle) {
        if (newTitle == null || newTitle.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = newTitle;
    }

    void updateDescription(String newDescription) {
        if (newDescription != null && newDescription.length() > 500) {
            throw new IllegalArgumentException("Description too long");
        }
        this.description = newDescription;
    }

    void updateRelativeDate(Integer newRelativeDate) {
        if (newRelativeDate == null || newRelativeDate < 1) {
            throw new IllegalArgumentException("RelativeDate must be at least 1");
        }
        this.relativeDate = newRelativeDate;
    }
}
