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
    @Column(nullable = false)
    private ItineraryType type;

    @Column(name = "relative_date", nullable = false)
    private Integer relativeDate;

    @Embedded
    private TimeSlot timeSlot;

    @Column
    private String description;

    ItineraryItem(String title, ItineraryType type, Integer relativeDate, TimeSlot timeSlot, String description) {
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

    void updateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title;
    }

    void updateDescription(String description) {
        if (description != null && description.length() > 500) {
            throw new IllegalArgumentException("Description too long");
        }
        this.description = description;
    }

    void updateRelativeDate(Integer relativeDate) {
        this.relativeDate = relativeDate;
    }
}
