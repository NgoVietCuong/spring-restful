package com.nvc.spring_boot.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetaDTO {
    private int currentPage;
    private int itemsPerPage;
    private int totalPages;
    private long totalItems;
}
