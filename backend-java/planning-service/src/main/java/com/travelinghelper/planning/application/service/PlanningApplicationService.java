package com.travelinghelper.planning.application.service;

import com.travelinghelper.planning.application.dto.item.AddItemRequest;
import com.travelinghelper.planning.application.dto.item.ItemResponse;
import com.travelinghelper.planning.application.dto.item.UpdateItemRequest;
import com.travelinghelper.planning.application.dto.plan.CreatePlanRequest;
import com.travelinghelper.planning.application.dto.plan.PlanResponse;
import com.travelinghelper.planning.application.dto.plan.UpdatePlanRequest;
import com.travelinghelper.planning.application.exception.PlanOwnershipException;
import com.travelinghelper.planning.domain.model.ItineraryItem;
import com.travelinghelper.planning.domain.model.TimeSlot;
import com.travelinghelper.planning.domain.model.TravelPlan;
import com.travelinghelper.planning.domain.repository.TravelPlanRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanningApplicationService {

    private final TravelPlanRepository planRepository;

    @Transactional
    public PlanResponse createPlan(CreatePlanRequest request, String userId) {
        TravelPlan plan = (request.startDate() != null && request.endDate() != null)
            ? TravelPlan.precise(userId, request.title(), request.startDate(), request.endDate())
            : TravelPlan.fuzzy(userId, request.title(), request.totalDays());

        plan.changeVisibility(request.visibility());

        planRepository.save(plan);

        return mapToPlanResponse(plan, false);
    }

    @Transactional
    public void addItemToPlan(String planId, AddItemRequest request, String userId) {
        TravelPlan plan = planRepository.findById(planId).orElseThrow();

        validateOwnership(plan, userId);

        TimeSlot timeSlot = (request.startTime() != null && request.endTime() != null)
            ? TimeSlot.precise(request.startTime(), request.endTime())
            : TimeSlot.fuzzy(request.timePeriod());

        plan.addItemByRelativeDate(request.title(), request.type(), request.relativeDate(), timeSlot, request.description());

        planRepository.save(plan);
    }

    @Transactional
    public void updateItemInPlan(String planId, String itemId, UpdateItemRequest request, String userId) {
        // Aggregate Root
        TravelPlan plan = planRepository.findById(planId)
            .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        validateOwnership(plan, userId);

        // Value Object
        TimeSlot newTimeSlot = (request.startTime() != null && request.endTime() != null)
            ? TimeSlot.precise(request.startTime(), request.endTime())
            : (request.timePeriod() != null)
            ? TimeSlot.fuzzy(request.timePeriod())
            : null;

        // Update the item by aggregate root (Support partial modification by checking null value)
        if (request.title() != null) plan.updateItemTitle(itemId, request.title());
        if (request.description() != null) plan.updateItemDescription(itemId, request.description());
        if (newTimeSlot != null) plan.updateItemTimeSlot(itemId, newTimeSlot);
        if (request.relativeDate() != null) plan.updateItemRelativeDate(itemId, request.relativeDate());

        // Save
        planRepository.save(plan);
    }

    @Transactional
    public void updatePlanInfo(String planId, UpdatePlanRequest request, String userId) {
        TravelPlan plan = planRepository.findById(planId)
            .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        validateOwnership(plan, userId);

        if (request.title() != null) plan.updateTitle(request.title());

        if (request.startDate() != null && request.endDate() != null) plan.updatePreciseDates(request.startDate(), request.endDate());
        else if (request.totalDays() != null) plan.updateTotalDays(request.totalDays());

        if (request.visibility() != null) plan.changeVisibility(request.visibility());

        planRepository.save(plan);
    }

    @Transactional
    public void removeItemFromPlan(String planId, String itemId, String userId) {
        TravelPlan plan = planRepository.findById(planId)
            .orElseThrow(() -> new IllegalArgumentException("Plan not found"));
        validateOwnership(plan, userId);
        plan.removeItem(itemId);
        planRepository.save(plan);
    }

    @Transactional
    public void deletePlan(String planId, String userId) {
        TravelPlan plan = planRepository.findById(planId)
            .orElseThrow(() -> new IllegalArgumentException("Plan not found"));
        validateOwnership(plan, userId);
        planRepository.deleteById(planId);
    }

    @Transactional(readOnly = true)
    public List<PlanResponse> getPlanSummariesByUserId(String userId) {
        return planRepository.findByUserId(userId).stream()
            .map(plan -> mapToPlanResponse(plan, false))
            .toList();
    }

    @Transactional(readOnly = true)
    public PlanResponse getPlanDetail(String planId, String userId) {
        TravelPlan plan = planRepository.findWithItemsById(planId)
            .orElseThrow(() -> new IllegalArgumentException("Plan not found"));
        validateOwnership(plan, userId);
        return mapToPlanResponse(plan, true);
    }

    @Transactional(readOnly = true)
    public List<ItemResponse> getItemsByPlanId(String planId, String userId) {
        TravelPlan plan = planRepository.findById(planId)
            .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        validateOwnership(plan, userId);

        return plan.getItems().stream()
            .map(item -> mapToItemResponse(item, plan))
            .toList();
    }

    private void validateOwnership(TravelPlan plan, String userId) {
        if (!plan.getUserId().equals(userId)) {
            throw new PlanOwnershipException("Permission denied: You do not own this plan.");
        }
    }

    // Mappers
    private PlanResponse mapToPlanResponse(TravelPlan plan, boolean includeItems) {
        return PlanResponse.builder()
            .id(plan.getId())
            .userId(plan.getUserId())
            .title(plan.getTitle())
            .startDate(plan.getStartDate())
            .endDate(plan.getEndDate())
            .totalDays(plan.getTotalDays())
            .visibility(plan.getVisibility())
            .isPrecise(plan.isPrecise())
            .items(
                includeItems
                ? plan.getItems().stream()
                    .map(item -> mapToItemResponse(item, plan))
                    .toList()
                : null
            )
            .build();
    }

    private ItemResponse mapToItemResponse(ItineraryItem item, TravelPlan plan) {
        // Compute the absolute date for item
        LocalDate absoluteDate = (plan.getStartDate() != null)
            ? plan.getStartDate().plusDays(item.getRelativeDate() - 1)
            : null;

        return ItemResponse.builder()
            .id(item.getId())
            .title(item.getTitle())
            .type(item.getType())
            .description(item.getDescription())
            .relativeDate(item.getRelativeDate())
            .date(absoluteDate)
            .startTime(item.getTimeSlot().getStartTime())
            .endTime(item.getTimeSlot().getEndTime())
            .timePeriod(item.getTimeSlot().getTimePeriod())
            .isPreciseDate(plan.isPrecise())
            .isPreciseTime(item.isPreciseTime())
            .build();
    }
}
