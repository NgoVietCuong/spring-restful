package com.nvc.spring_boot.dto.user.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CreateUserResponse extends BaseUserResponse {
     Instant createdAt;
}
