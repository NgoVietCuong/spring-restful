package com.nvc.spring_boot.dto.auth.response;

import com.nvc.spring_boot.entity.Role;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
public class LoginResponse {
    private String accessToken;
    private UserLogin user;

    @Data
    @SuperBuilder
    @NoArgsConstructor
    public static class UserInToken {
        private Long id;
        private String name;
        private String email;
    }

    @Data
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    public static class UserLogin extends UserInToken {
        private Role role;
    }
}
