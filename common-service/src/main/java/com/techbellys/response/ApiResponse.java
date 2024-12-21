package com.techbellys.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {

    private boolean success;
    private int status;
    private T data;

    public static <T> ApiResponse<T> success(final T data) {
        return ApiResponse.<T>builder().success(true).data(data).status(200).build();
    }

    public static <T> ApiResponse<T> failure(final T data) {
        return ApiResponse.<T>builder().success(false).data(data).status(400).build();
    }

    public static <T> ApiResponse<T> failure() {
        return ApiResponse.<T>builder().success(false).data(null).status(400).build();
    }

    public static <T> ApiResponse<T> success() {
        return ApiResponse.<T>builder().success(true).status(200).build();
    }
}
