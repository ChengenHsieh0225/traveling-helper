package com.travelinghelper.social.application.service;

import com.travelinghelper.social.domain.model.SharedPlan;
import com.travelinghelper.social.domain.repository.SharedPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InteractionService {

    private final SharedPlanRepository planRepository;

    @Transactional
    public void likePlan(String planId, String userId) {
        SharedPlan plan = planRepository.findById(planId)
            .orElseThrow(() -> new IllegalArgumentException("SharedPlan not found"));
        plan.incrementLikeCount();
        planRepository.save(plan);
    }

    @Transactional
    public void unlikePlan(String planId, String userId) {
        SharedPlan plan = planRepository.findById(planId)
            .orElseThrow(() -> new IllegalArgumentException("SharedPlan not found"));
        plan.decrementLikeCount();
        planRepository.save(plan);
    }

    @Transactional
    public void copyPlan(String planId, String userId) {
        SharedPlan plan = planRepository.findById(planId)
            .orElseThrow(() -> new IllegalArgumentException("SharedPlan not found"));
        plan.incrementCopyCount();
        planRepository.save(plan);
    }
}
