package com.travelinghelper.planning.controller;

import com.travelinghelper.planning.application.dto.plan.CreatePlanRequest;
import com.travelinghelper.planning.application.service.PlanningApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanningController {

    private final PlanningApplicationService planningService;

    @PostMapping
    public ResponseEntity<String> createPlan(
        @Valid @RequestBody CreatePlanRequest request
//        @AuthenticationPrincipal Jwt jwt // Spring would extract the Bearer Token for Header automatically
    ) {
        // align with the jwt token generation logic within auth-service
//        String userId = jwt.getSubject();

        String userId = "waynehsieh";
        String planId = planningService.createPlan(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(planId);
    }

    @GetMapping("/test")
    public String test() {
        return "Hello!";
    }

}
