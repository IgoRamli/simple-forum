package com.example.simpleforum.beans;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class PasswordEncoder {
    private static final int SALT_LENGTH = 16;
    private static final int HASH_LENGTH = 64;
    private static final int ITERATIONS = 10;

    public Argon2PasswordEncoder encoder() {
        return new Argon2PasswordEncoder(SALT_LENGTH, HASH_LENGTH, 1, 60000, ITERATIONS);
    }
}
