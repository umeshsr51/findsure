package com.findsure.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

    @JsonProperty("error")
    private final ErrorBody error;

    @Getter
    @AllArgsConstructor
    public static class ErrorBody {
        private String code;
        private String message;
        private int status;
    }

    public static ErrorResponse of(String code, String message, int status) {
        return new ErrorResponse(new ErrorBody(code, message, status));
    }
}
