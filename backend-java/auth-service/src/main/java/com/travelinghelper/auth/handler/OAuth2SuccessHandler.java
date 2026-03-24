package com.travelinghelper.auth.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtEncoder jwtEncoder;
    public OAuth2SuccessHandler(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Instant now = Instant.now();
        long expiry = 36000L; // Token 有效期 10 小時

        // 1. 建立 JWT 的內容 (Payload)
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiry))
            .subject(authentication.getName()) // 使用者唯一識別碼
            .claim("scope", "ROLE_USER")      // 可以自定義額外資訊
            .build();

        // 2. 使用 JwtEncoder 進行 RS256 簽署
        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        // 3. 前後端分離的常見作法：重導向回前端並帶上 Token (URL Fragment 或 Query)
        // 假設你的前端跑在 3000 埠
//        String targetUrl = "http://localhost:8080/auth/test?token=" + token;
//
//        System.out.println("token: " + token);
//
//        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
