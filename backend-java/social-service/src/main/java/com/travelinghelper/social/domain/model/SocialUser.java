package com.travelinghelper.social.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "social_users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialUser {

    @Id
    private String userId;

    private String name;
    private String avatar;

}
