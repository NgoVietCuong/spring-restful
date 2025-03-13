package com.nvc.spring_boot.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder()
public class PaginationResponse {
    private PaginationMeta meta;
    private Object content;

    @Data
    @Builder()
    public static class PaginationMeta {
        private int currentPage;
        private int itemsPerPage;
        private int totalPages;
        private long totalItems;
    }
}
