package com.travelinghelper.planning.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeSlot {

    @Enumerated(EnumType.STRING)
    @Column(name = "time_period")
    private TimePeriod timePeriod;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    private TimeSlot(TimePeriod timePeriod, LocalTime startTime, LocalTime endTime) {
        this.timePeriod = timePeriod;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static TimeSlot fuzzy(TimePeriod timePeriod) {
        return new TimeSlot(timePeriod, null, null);
    }

    public static TimeSlot precise(LocalTime start, LocalTime end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("The start time should be early than the end time");
        }
        return new TimeSlot(calculatePeriod(start), start, end);
    }

    private static TimePeriod calculatePeriod(LocalTime time) {
        int hour = time.getHour();
        if (hour >= 5 && hour < 12) return TimePeriod.MORNING;
        if (hour >= 12 && hour < 18) return TimePeriod.AFTERNOON;
        return TimePeriod.EVENING;
    }

    public boolean isPrecise() {
        return this.startTime != null && this.endTime != null;
    }
}
