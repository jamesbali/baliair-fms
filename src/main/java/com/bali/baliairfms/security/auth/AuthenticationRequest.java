package com.bali.baliairfms.security.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
