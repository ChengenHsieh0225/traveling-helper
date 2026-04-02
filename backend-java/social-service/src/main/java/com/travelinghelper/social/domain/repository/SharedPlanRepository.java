package com.travelinghelper.social.domain.repository;

import com.travelinghelper.social.domain.model.SharedPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedPlanRepository extends JpaRepository<SharedPlan, String> {
    // Default methods provided by JpaRepository: save, findById, deleteById
}
