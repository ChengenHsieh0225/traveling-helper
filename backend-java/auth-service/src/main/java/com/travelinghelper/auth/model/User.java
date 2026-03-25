package com.travelinghelper.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    @Column(name = "given_name", nullable = false)
    private String givenName;

    @Column(name = "family_name", nullable = false)
    private String familyName;

    @Column
    private String email;

    @Column
    private String avatar;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "last_login")
    private Instant lastLogin;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.lastLogin = Instant.now();
    }

    public void markLogin() {
        this.lastLogin = Instant.now();
    }

    public String getDisplayedName() {
         if (this.familyName == null) return givenName;
         if (this.givenName == null) return familyName;
         return givenName + " " + familyName;
    }
}
