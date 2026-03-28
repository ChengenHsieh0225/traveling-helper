package com.travelinghelper.planning.domain.repository;

import com.travelinghelper.planning.domain.model.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelPlanRepository extends JpaRepository<TravelPlan, String> {

    List<TravelPlan> findByUserId(String userId);

    // fuzzy search
    List<TravelPlan> findByTitleContaining(String title);

    // JpaRepository provided some default methods: save, findById, deleteById
}