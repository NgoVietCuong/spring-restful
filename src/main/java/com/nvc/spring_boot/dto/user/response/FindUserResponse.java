package com.nvc.spring_boot.dto.user.response;

import com.nvc.spring_boot.util.constant.Gender;
import lombok.Data;

import java.time.Instant;

@Data
public class FindUserResponse {
    private Long id;
    private String name;
    private String email;
    private Gender gender;
    private String address;
    private Instant createdAt;
    private Instant updatedAt;
    private UserCompany company;

    @Data
    public static class UserCompany {
        private Long id;
        private String name;
    }
}
