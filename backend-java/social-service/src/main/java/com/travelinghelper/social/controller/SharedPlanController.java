package com.travelinghelper.social.controller;

import com.travelinghelper.social.application.dto.SharedPlanItineraryResponse;
import com.travelinghelper.social.application.dto.SharedPlanSummaryResponse;
import com.travelinghelper.social.application.query.SharedPlanQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class SharedPlanController {

    private final SharedPlanQueryService planQueryService;

    @GetMapping
    public ResponseEntity<List<SharedPlanSummaryResponse>> getPlans(
        @AuthenticationPrincipal Jwt jwt
    ) {
        String userId = jwt.getSubject();
        return ResponseEntity.ok(
            planQueryService.listSharedPlans(userId)
        );
    }

    @GetMapping("/{planId}/items")
    public ResponseEntity<List<SharedPlanItineraryResponse>> getItineraryListFromPlan(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable String planId
    ) {
        return ResponseEntity.ok(
            planQueryService.getItineraryListFromPlan(planId)
        );
    }
}
