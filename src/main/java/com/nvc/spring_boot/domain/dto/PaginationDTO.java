package com.nvc.spring_boot.domain.dto;

import lombok.Data;

@Data
public class PaginationDTO {
    private MetaDTO meta;
    private Object content;
}
