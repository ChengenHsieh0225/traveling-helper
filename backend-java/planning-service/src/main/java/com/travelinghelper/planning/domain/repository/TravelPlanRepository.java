package com.travelinghelper.planning.domain.repository;

import com.travelinghelper.planning.domain.model.TravelPlan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelPlanRepository extends JpaRepository<TravelPlan, String> {

    // Prevent N+1 problem by using EntityGraph
    // @EntityGraph(attributePaths = {"items"})
    // List<TravelPlan> findAllByUserId(String userId);

    List<TravelPlan> findByUserId(String userId);

    @EntityGraph(attributePaths = {"items"})
    Optional<TravelPlan> findWithItemsById(String id);

    // fuzzy search
    List<TravelPlan> findByTitleContaining(String title);

    // JpaRepository provided some default methods: save, findById, deleteById
}