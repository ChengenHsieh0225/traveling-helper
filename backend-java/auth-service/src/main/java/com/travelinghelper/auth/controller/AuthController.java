package com.travelinghelper.auth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @GetMapping("/callback")
    public String getUserInfo(@RequestParam String token) {
        return "Hello! This is your token: " + token;
    }
    @PostMapping("/users")
    public void createUser() {

    }
    @PutMapping("/users")
    public void updateUser(String name) {

    }

    @GetMapping("/health")
    public String healthCheck() {
        return "OK";
    }
}
