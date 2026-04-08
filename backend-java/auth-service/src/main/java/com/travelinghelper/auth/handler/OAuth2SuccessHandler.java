package com.travelinghelper.auth.handler;

import com.travelinghelper.auth.model.User;
import com.travelinghelper.auth.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    public OAuth2SuccessHandler(JwtEncoder jwtEncoder, UserRepository userRepository) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String sub = oAuth2User.getAttribute("sub");
        if (sub == null) throw new IllegalStateException("OAuth2 sub (ID) is missing");

        String email = Optional.ofNullable(oAuth2User.getAttribute("email"))
            .map(Object::toString)
            .orElse("unknown@unknown.com");
        String givenName = Optional.ofNullable(oAuth2User.getAttribute("given_name"))
            .map(Object::toString)
            .orElse("");
        String familyName = Optional.ofNullable(oAuth2User.getAttribute("family_name"))
            .map(Object::toString)
            .orElse("");
        String avatar = Optional.ofNullable(oAuth2User.getAttribute("picture"))
            .map(Object::toString)
            .orElse("");

        User user = this.userRepository.findById(sub)
            .map(existingUser -> {
                existingUser.markLogin();
                return this.userRepository.save(existingUser);
            })
            .orElseGet(() -> {
                User newUser = User.builder()
                    .id(sub)
                    .givenName(givenName)
                    .familyName(familyName)
                    .email(email)
                    .avatar(avatar)
                    .build();
                return this.userRepository.save(newUser);
            });

        String token = generateToken(user);


        // 3. Redirect to frontend with JWT token in the URL
        // String targetUrl = "http://localhost:3000/login-success#token=" + token;

        // Temporary for testing purposes
        String targetUrl = "http://localhost:8080/auth/test?token=" + token + "&name=" + user.getDisplayedName();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String generateToken(User user) {
        Instant now = Instant.now();

        // 1. Build JWT claims (payload)
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(36000L)) // Valid for 10 hours
            .subject(user.getId()) // User identifier
            .claim("name", user.getDisplayedName())      // Custom claims
            .build();

        // 2. Sign and encode JWT using RS256
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
