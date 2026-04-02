package com.travelinghelper.social.application.service;

import com.travelinghelper.social.domain.event.EventItemRecord;
import com.travelinghelper.social.domain.event.PlanPublishedEvent;
import com.travelinghelper.social.domain.model.*;
import com.travelinghelper.social.domain.repository.SharedPlanRepository;
import com.travelinghelper.social.domain.repository.SocialUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SyncService {

    private final SocialUserRepository userRepository;
    private final SharedPlanRepository planRepository;

    @Transactional
    public void syncFromEvent(PlanPublishedEvent event) {
        SharedPlan plan = planRepository.findById(event.id()).orElse(
            SharedPlan.create(
                event.id(), event.userId(), event.title(), event.totalDays(), SocialVisibility.fromString(event.visibility())
            )
        );
        List<ItineraryData> dataList = event.items().stream()
            .map(this::mapToDomainData)
            .toList();

        plan.syncItinerary(dataList);
        planRepository.save(plan);
    }

    private ItineraryData mapToDomainData(EventItemRecord record) {
        String duration = calculateDuration(record.startTime(), record.endTime());

        return new ItineraryData(
            record.id(),
            record.title(),
            record.relativeDate(),
            record.description(),
            SharedItineraryType.fromString(record.type()),
            TimePeriod.fromString(record.timePeriod()),
            duration
        );
    }

    private static String calculateDuration(LocalTime start, LocalTime end) {
        if (start == null || end == null) return "";
        long minutes = java.time.Duration.between(start, end).toMinutes();
        long h = minutes / 60;
        long m = minutes % 60;
        return h > 0 ? String.format("%dh %dm", h, m) : String.format("%dm", m);
    }
}
