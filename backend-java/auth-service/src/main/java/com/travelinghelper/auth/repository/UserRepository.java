package com.travelinghelper.auth.repository;

import com.travelinghelper.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String name);
    boolean existsByEmail(String email);
}
