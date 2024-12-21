package com.techbellys.utils;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.techbellys.entity.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JwtUtil {

    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${security.jwt.issuer}")
    private String jwtIssuer;

    public String createJwtToken(AppUser appUser) {
        Instant now = Instant.now();
        // Build JWT claims
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtIssuer) // Issuer of the token
                .issuedAt(now) // Token issued at
                .expiresAt(now.plusSeconds(24 * 3600)) // Token expiration (24 hours)
                .subject(appUser.getId()) // Subject of the token (user ID)
                .claim("username", appUser.getUsername()) // Username claim
                .claim("email", appUser.getEmail()) // Email claim
                .claim("full_name", appUser.getFull_name()) // Full name claim
                .claim("role", appUser.getRole()) // Role claim
                .build();

        // Configure JWT encoder
        NimbusJwtEncoder encoder = new NimbusJwtEncoder(
                new ImmutableSecret<>(jwtSecretKey.getBytes()));
        JwtEncoderParameters params = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        // Encode and return the JWT token
        return encoder.encode(params).getTokenValue();
    }
}
