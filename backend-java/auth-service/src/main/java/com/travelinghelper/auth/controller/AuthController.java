package com.travelinghelper.auth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @GetMapping("/test")
    public String getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getSubject();
        return "Hello, " + username + ". This is secured data.";
    }
}
