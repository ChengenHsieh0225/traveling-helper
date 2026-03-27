package com.travelinghelper.planning.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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

    @Column
    private LocalDate date;

    @Column(name = "relative_date")
    private Integer relativeDate;

    @Embedded
    private TimeSlot timeSlot;

    @Column
    private String description;

    ItineraryItem(String title, ItineraryType type, LocalDate date, Integer relativeDate, TimeSlot timeSlot, String description) {
        if (date == null && relativeDate == null) {
            throw new IllegalArgumentException("Either 'date' or 'relativeDate' must be provided.");
        }
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.type = type;
        this.date = date;
        this.relativeDate = relativeDate;
        this.timeSlot = timeSlot;
        this.description = description;
    }

    static ItineraryItem createWithDate(String title, ItineraryType type, LocalDate date, TimeSlot timeSlot, String description) {
        return new ItineraryItem(title, type, date, null, timeSlot, description);
    }

    static ItineraryItem createWithRelativeDate(String title, ItineraryType type, Integer relativeDate, TimeSlot timeSlot, String description) {
        return new ItineraryItem(title, type, null, relativeDate, timeSlot, description);
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

    void updateDateContext(LocalDate date, Integer relativeDate) {
        this.date = date;
        this.relativeDate = relativeDate;
    }
}
