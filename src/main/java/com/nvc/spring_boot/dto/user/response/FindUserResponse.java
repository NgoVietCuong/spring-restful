package com.nvc.spring_boot.dto.user.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class FindUserResponse extends BaseUserResponse {
    Instant createdAt;
    Instant updatedAt;
}
