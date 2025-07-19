package com.bali.baliairfms.security.user;


import lombok.*;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write");

    @Getter
    private final String permission;
}
