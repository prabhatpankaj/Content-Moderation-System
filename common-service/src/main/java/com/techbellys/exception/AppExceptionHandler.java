package com.techbellys.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldValidationError> errorList = new ArrayList<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            FieldValidationError error = new FieldValidationError();
            error.setField(fieldError.getField());
            error.setMessage(fieldError.getDefaultMessage());
            error.setRejectedValue(fieldError.getRejectedValue());
            errorList.add(error);
        }
        CustomError customError = new CustomError(false, BAD_REQUEST.value(), "Validation Errors", errorList);
        return ResponseEntity.status(OK).body(customError);
    }

    @ExceptionHandler({ApplicationException.class})
    public ResponseEntity<Reason> handleException(ApplicationException exception) {
        log.warn(exception.getMessage());
        Reason reason = Reason.from(exception);
        return ResponseEntity.status(exception.getApplicationCode().httpCode()).body(reason);
    }
}