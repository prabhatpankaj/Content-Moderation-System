package com.techbellys.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class CustomError<T> {
    private boolean success;
    private int status;
    private T data;
    private List<FieldValidationError> errors;
}
