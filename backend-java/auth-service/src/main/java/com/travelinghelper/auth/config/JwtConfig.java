package com.travelinghelper.auth.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.util.StreamUtils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Configuration
public class JwtConfig {
    @Value("${app.key.private-path:certs/private_key.der}")
    private String privateKeyPath;
    @Value("${app.key.public-path:certs/public_key.der}")
    private String publicKeyPath;
    @Bean
    public JwtEncoder jwtEncoder() throws Exception {
        RSAPrivateKey privateKey = (RSAPrivateKey) readPrivateKey(privateKeyPath);
        RSAPublicKey publicKey = (RSAPublicKey) readPublicKey(publicKeyPath);

        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    private PrivateKey readPrivateKey(String path) throws Exception {
        byte[] keyBytes = StreamUtils.copyToByteArray(new ClassPathResource(path).getInputStream());
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }
    private PublicKey readPublicKey(String path) throws Exception {
        byte[] keyBytes = StreamUtils.copyToByteArray(new ClassPathResource(path).getInputStream());
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
}
