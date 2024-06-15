package com.example.simpleforum.utils;

import com.example.simpleforum.beans.AuthUserBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {
    public AuthUserBean getAuthDetails(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return AuthUserBean.builder()
                .id(jwt.getSubject())
                .username(jwt.getClaimAsString("preferred_username"))
                .firstName(jwt.getClaimAsString("given_name"))
                .lastName(jwt.getClaimAsString("family_name"))
                .build();
    }
}
