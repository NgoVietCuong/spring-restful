package com.nvc.spring_boot.dto.user.response;

import com.nvc.spring_boot.util.constant.Gender;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class BaseUserResponse {
    Long id;
    String name;
    String email;
    Gender gender;
    String address;
    UserCompany company;
    UserRole role;

    @Data
    @Builder
    public static class UserCompany {
        Long id;
        String name;
    }

    @Data
    @Builder
    public static class UserRole {
        Long id;
        String name;
    }
}
