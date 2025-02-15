package com.nvc.spring_boot.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaginationDTO {
    private MetaDTO meta;
    private Object content;
}
