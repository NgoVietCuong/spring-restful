package com.nvc.spring_boot.entity.response;

import com.nvc.spring_boot.util.constant.Gender;
import lombok.Data;

import java.time.Instant;

@Data
public class ResUpdateUserDTO {
    private Long id;
    private String name;
    private Gender gender;
    private String address;
    private Instant updatedAt;
    private UserCompany company;

    @Data
    public static class UserCompany {
        private Long id;
        private String name;
    }
}
