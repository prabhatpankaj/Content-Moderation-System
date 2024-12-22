package com.techbellys.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
public class ApiResponse<T> {

    private boolean success;
    private int status;
    private T data;

    @Data
    @Builder
    public static class PaginatedData<T> {
        private List<T> content; // Only the list of items
        private Pagination pagination; // Pagination metadata
    }

    @Data
    @Builder
    public static class Pagination {
        private int pageNumber;
        private int pageSize;
        private long totalElements;
        private int totalPages;
        private boolean isLast;
    }

    // Success response without pagination
    public static <T> ApiResponse<T> success(final T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .status(200)
                .build();
    }

    // Success response with a paginated Page object
    public static <T> ApiResponse<PaginatedData<T>> success(final Page<T> page) {
        PaginatedData<T> paginatedData = PaginatedData.<T>builder()
                .content(page.getContent()) // Include only the content
                .pagination(Pagination.builder()
                        .pageNumber(page.getNumber())
                        .pageSize(page.getSize())
                        .totalElements(page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .isLast(page.isLast())
                        .build())
                .build();

        return ApiResponse.<PaginatedData<T>>builder()
                .success(true)
                .data(paginatedData)
                .status(200)
                .build();
    }

    // Failure response with custom data
    public static <T> ApiResponse<T> failure(final T data) {
        return ApiResponse.<T>builder()
                .success(false)
                .data(data)
                .status(400)
                .build();
    }

    // Failure response without custom data
    public static <T> ApiResponse<T> failure() {
        return ApiResponse.<T>builder()
                .success(false)
                .data(null)
                .status(400)
                .build();
    }

    // General success response without data
    public static <T> ApiResponse<T> success() {
        return ApiResponse.<T>builder()
                .success(true)
                .status(200)
                .build();
    }
}
