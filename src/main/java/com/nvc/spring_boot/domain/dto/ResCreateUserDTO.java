package com.nvc.spring_boot.domain.dto;

import com.nvc.spring_boot.util.constant.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResCreateUserDTO {
    private Long id;
    private String name;
    private String email;
    private Gender gender;
    private String address;
    private Instant createdAt;
}
