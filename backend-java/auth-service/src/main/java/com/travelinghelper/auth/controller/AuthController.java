package com.travelinghelper.auth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @GetMapping("/test")
    public String getUserInfo(@RequestParam String token, @RequestParam String name) {
        return "Hello, " + name + ". This is your token: " + token;
    }
    @PostMapping("/user")
    public void createUser() {

    }
    @PutMapping("/user")
    public void updateUser(String name) {

    }
}
