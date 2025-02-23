package com.nvc.spring_boot.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ResLoginDTO {
    private String accessToken;
    private UserLogin user;

    @Data
    @AllArgsConstructor
    public static class UserLogin {
        private Long id;
        private String name;
        private String email;
    }
}
