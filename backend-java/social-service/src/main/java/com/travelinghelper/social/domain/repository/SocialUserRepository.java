package com.travelinghelper.social.domain.repository;

import com.travelinghelper.social.domain.model.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialUserRepository extends JpaRepository<SocialUser, String> {
    // Default methods provided by JpaRepository: save, findById, deleteById
}
