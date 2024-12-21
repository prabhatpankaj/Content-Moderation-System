package com.techbellys.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class ApplicationException extends RuntimeException {

    public static final ApplicationCode SERVER_ERROR = new ApplicationCodeImpl("500", "Internal Server Error");
    private final ApplicationCode applicationCode;
    private String description;

    private HashMap<String, String> details;
    private List<ApplicationException> nestedExceptions = new ArrayList<>();

    public ApplicationException() {
        this(SERVER_ERROR.message());
    }

    public ApplicationException(String description) {
        super(description);
        applicationCode = SERVER_ERROR;
    }

    public ApplicationException(ApplicationCode applicationCode) {
        super(applicationCode.message());
        this.applicationCode = applicationCode;
    }

    public ApplicationException(ApplicationCode applicationCode, String description) {
        super(applicationCode.code() + "-(" + applicationCode.subCode() + ")-" + applicationCode.message() + "-" + description);
        this.applicationCode = applicationCode;
        this.description = description;
    }

    public ApplicationException(ApplicationCode applicationCode, String description, HashMap<String, String> details) {
        super(applicationCode.code() + "-(" + applicationCode.subCode() + ")-" + applicationCode.message() + "-" + description);
        this.applicationCode = applicationCode;
        this.description = description;
        this.details = details;
    }

    public ApplicationException(String errorCode, String message, String description) {
        this(new ApplicationCodeImpl(errorCode, message), description);
    }


    public ApplicationException(String errorCode, String subCode, String message, int http, String description) {
        this(new ApplicationCodeImpl(errorCode, subCode, message, http), description);
    }

    public ApplicationException(String errorCode,int http, String message, String description) {
        this(new ApplicationCodeImpl(errorCode, message,http), description);
    }

    public ApplicationException(ApplicationCode applicationCode, Throwable cause) {
        super(applicationCode.message(), cause);
        this.applicationCode = applicationCode;
    }

    public ApplicationException(String description, Throwable cause) {
        super(description, cause);
        this.description = description;
        this.applicationCode = SERVER_ERROR;
    }

    public void addNestedException(ApplicationException e) {
        nestedExceptions.add(e);
    }

    public List<ApplicationException> getNestedExceptions() {
        return nestedExceptions;
    }

    public ApplicationCode getApplicationCode() {
        return applicationCode;
    }

    public String getDescription() {
        return description;
    }

    public HashMap<String, String> getDetails() {
        return details;
    }


    @Getter
    @Setter
    public static class ApplicationCodeImpl implements ApplicationCode {

        private String code;
        private String subCode;
        private String message;
        private int http;

        public ApplicationCodeImpl(String code, String subCode, String message) {
            this.code = code;
            this.subCode = subCode;
            this.message = message;
            this.http = 500;
        }

        public ApplicationCodeImpl(String code, String subCode, String message, int http) {
            this.code = code;
            this.subCode = subCode;
            this.message = message;
            this.http = http;
        }

        public ApplicationCodeImpl(String code, String message) {
            this.code = code;
            this.subCode = code;
            this.message = message;
            this.http = 500;
        }

        public ApplicationCodeImpl(String code, String message, int http) {
            this.code = code;
            this.subCode = code;
            this.message = message;
            this.http = http;
        }


        @Override
        public String code() {
            return code;
        }

        @Override
        public String subCode() {
            return subCode;
        }

        @Override
        public String message() {
            return message;
        }

        @Override
        public HttpStatus httpCode() {
            return null;
        }
    }

    public static class Builder {

        private ApplicationException exception;
        private List<ApplicationException> nested = new ArrayList<>();

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder root(ApplicationCode code) {
            return this.root(code, null);
        }

        public Builder root(ApplicationCode code, String descr) {
            this.exception = new ApplicationException(code, descr);
            return this;
        }

        public Builder error(ApplicationCode code) {
            return this.error(code, (String) null);
        }

        public Builder error(ApplicationCode code, String descr) {
            this.nested.add(new ApplicationException(code, descr));
            return this;
        }

        public Builder error(Exception e) {
            this.nested.add(new ApplicationException(e.getMessage(), e));
            return this;
        }

        public Builder error(ApplicationCode code, Exception e) {
            this.nested.add(new ApplicationException(code, e));
            return this;
        }

        public boolean isErrorExist() {
            return exception != null || nested.size() > 0;
        }

        public Optional<ApplicationException> build() {
            if (!isErrorExist()) return Optional.empty();
            if (nested.size() == 0) return Optional.ofNullable(exception);
            if (exception == null && nested.size() == 1) return Optional.of(nested.iterator().next());
            if (exception == null) {
                StringBuilder stringBuilder = new StringBuilder();
                nested.stream().forEach(n -> stringBuilder.append(n.getMessage()).append(" : "));
                this.exception = new ApplicationException(stringBuilder.toString());
            }
            this.exception.nestedExceptions = nested;
            return Optional.of(exception);
        }
    }
}
