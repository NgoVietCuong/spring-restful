package com.nvc.spring_boot.dto;

import lombok.Data;

@Data
public class PaginationDTO {
    private MetaDTO meta;
    private Object content;

    @Data
    public static class MetaDTO {
        private int currentPage;
        private int itemsPerPage;
        private int totalPages;
        private long totalItems;
    }
}
