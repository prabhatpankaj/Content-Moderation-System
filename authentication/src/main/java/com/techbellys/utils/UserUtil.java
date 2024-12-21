package com.techbellys.utils;

import com.techbellys.dto.AppUserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {
    public AppUserDto extractUserFromAuth(Authentication authentication) {
        // Extract user details from Authentication
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Unauthorized access");
        }

        // Assuming the JWT holds user details in the principal or claims
        var principal = authentication.getPrincipal();
        // Handle Jwt principal
        if (principal instanceof Jwt jwt) {
            AppUserDto user = new AppUserDto();
            user.setId(jwt.getClaimAsString("sub")); // Extract user ID (sub claim)
            user.setUsername(jwt.getClaimAsString("username")); // Extract username
            user.setFull_name(jwt.getClaimAsString("full_name"));
            user.setRole(jwt.getClaimAsString("role"));
            user.setEmail(jwt.getClaimAsString("email"));
            return user;
        }

        throw new RuntimeException("Failed to extract user details from authentication");
    }
}
