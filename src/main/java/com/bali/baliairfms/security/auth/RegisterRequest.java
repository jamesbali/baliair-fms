package com.bali.baliairfms.security.auth;

public record RegisterRequest(
        String firstName,
        String lastName,
        String username,
        String password
) {
}
