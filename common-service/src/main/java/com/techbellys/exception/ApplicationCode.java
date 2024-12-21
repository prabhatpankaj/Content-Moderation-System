package com.techbellys.exception;

import org.springframework.http.HttpStatus;

public interface ApplicationCode {

    String code();
    String message();
    HttpStatus httpCode();

    default String subCode() {
        return code();
    }
}

