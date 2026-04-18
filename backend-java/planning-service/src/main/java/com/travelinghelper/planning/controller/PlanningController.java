package com.travelinghelper.planning.controller;

import com.travelinghelper.planning.application.dto.item.AddItemRequest;
import com.travelinghelper.planning.application.dto.item.UpdateItemRequest;
import com.travelinghelper.planning.application.dto.plan.CreatePlanRequest;
import com.travelinghelper.planning.application.dto.plan.PlanResponse;
import com.travelinghelper.planning.application.dto.plan.UpdatePlanRequest;
import com.travelinghelper.planning.application.service.PlanningApplicationService;
import com.travelinghelper.planning.domain.model.TravelPlan;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanningController {

    private final PlanningApplicationService planningService;

    // --- Travel Plan Operations ---
    @PostMapping
    public ResponseEntity<PlanResponse> createPlan(
        @Valid @RequestBody CreatePlanRequest request,
        @AuthenticationPrincipal Jwt jwt // Spring would extract the Bearer Token for Header automatically
    ) {
        // align with the jwt token generation logic within auth-service
        String userId = jwt.getSubject();

        PlanResponse response = planningService.createPlan(request, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PlanResponse>> getUserPlans(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(
            planningService.getPlanSummariesByUserId(userId)
        );
    }

    @GetMapping("/{planId}")
    public ResponseEntity<PlanResponse> getPlanDetail(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable String planId
    ) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(
            planningService.getPlanDetail(planId, userId)
        );
    }

    @PatchMapping("/{planId}")
    public ResponseEntity<Void> updatePlan(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable String planId,
        @Valid @RequestBody UpdatePlanRequest request
    ) {
        String userId = jwt.getSubject();
        planningService.updatePlanInfo(planId, request, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{planId}")
    public ResponseEntity<Void> deletePlan(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable String planId
    ) {
        String userId = jwt.getSubject();
        planningService.deletePlan(planId, userId);
        return ResponseEntity.noContent().build();
    }

    // --- Itinerary Items Operations ---

    @PostMapping("/{planId}/items")
    public ResponseEntity<Void> addItem(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable String planId,
        @Valid @RequestBody AddItemRequest request
    ) {
        String userId = jwt.getSubject();
        planningService.addItemToPlan(planId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{planId}/items/{itemId}")
    public ResponseEntity<Void> updateItem(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable String planId,
        @PathVariable String itemId,
        @RequestBody UpdateItemRequest request
    ) {
        String userId = jwt.getSubject();
        planningService.updateItemInPlan(planId, itemId, request, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{planId}/items/{itemId}")
    public ResponseEntity<Void> removeItem(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable String planId,
        @PathVariable String itemId
    ) {
        String userId = jwt.getSubject();
        planningService.removeItemFromPlan(planId, itemId, userId);
        return ResponseEntity.noContent().build();
    }

    // Health Check
    @GetMapping("/health")
    public String healthCheck() {
        return "planning-service is healthy!";
    }

    @GetMapping("/userId")
    public String jwtTest(
        @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();
        return "Your ID: " + userId;
    }
}
