package com.example.simpleforum.utils;

import com.example.simpleforum.beans.AuthUserBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthUtilsTest {
    @Autowired
    private AuthUtils authUtils;

    @Test
    void givenAuthenticatedUser_whenGetAuthDetails_verifyDetailsAreCorrect() {
        Authentication authentication = mock(Authentication.class);
        Jwt jwt = mock(Jwt.class);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn("user_id");
        when(jwt.getClaimAsString("preferred_username")).thenReturn("username");
        when(jwt.getClaimAsString("given_name")).thenReturn("First");
        when(jwt.getClaimAsString("family_name")).thenReturn("Name");

        AuthUserBean actual = authUtils.getAuthDetails(authentication);
        assertThat(actual.getId()).isEqualTo("user_id");
        assertThat(actual.getUsername()).isEqualTo("username");
        assertThat(actual.getFirstName()).isEqualTo("First");
        assertThat(actual.getLastName()).isEqualTo("Name");
    }
}
