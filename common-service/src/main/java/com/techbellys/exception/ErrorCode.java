package com.techbellys.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode implements ApplicationCode {

    EXTERNAL_SERVICE_BAD_REQUEST("E_MANDATE400", "Bad request ", HttpStatus.BAD_REQUEST),
    EXTERNAL_SERVICE_ERROR("ES400", "Service not available ", HttpStatus.INTERNAL_SERVER_ERROR),
    RESOURCE_NOT_FOUND("RNF404", "Not found", HttpStatus.NOT_FOUND),
    NO_PROVIDER_FOUND("OTP4221", "No OTP provider found", HttpStatus.UNPROCESSABLE_ENTITY ),
    OTP_NOT_FOUND("OTP4041",  "OTP Not Found", HttpStatus.NOT_FOUND),
    INVALID_OTP("OTP4002",  "Invalid OTP", HttpStatus.UNAUTHORIZED),
    IMMEDIATE_OTP_RETRY_EXCEPTION("OTP4031",  "You are re-trying too early. Please wait for sometime.", HttpStatus.FORBIDDEN),
    PROFILE_NOT_FOUND("PNF404", "Profile Not found", HttpStatus.NOT_FOUND),
    INVALID_DOCUMENT("DOC400", "Invalid file format or size. Please upload a valid PAN/Aadhaar image.", HttpStatus.BAD_REQUEST),
    OCR_INTERNAL_ERROR("OCR500", "Error While doing OCR", HttpStatus.INTERNAL_SERVER_ERROR),
    DOC_NOT_FOUND("DOC4042",  "Doc Not Found", HttpStatus.NOT_FOUND),
    TEMPLATE_NOT_FOUND("TMP404", "Template Not found", HttpStatus.NOT_FOUND);

    private String code;
    private String message;
    private HttpStatus httpCode;

    ErrorCode(String code, String message, HttpStatus httpCode) {
        this.code = code;
        this.message = message;
        this.httpCode = httpCode;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public HttpStatus httpCode() {
        return httpCode;
    }

    public boolean equals(ApplicationCode applicationCode) {
        return this.code.equalsIgnoreCase(applicationCode.code());
    }
}
