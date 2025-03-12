package com.hanghae.application.exception;

import com.hanghae.application.enums.ErrorCode;
import lombok.Getter;

/**
 * 요청 오류 Exception
 * 400 응답위해 Exception 구분
 */
@Getter
public class ApplicationRequestException extends RuntimeException {
    private final ErrorCode errorCode;

    public ApplicationRequestException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApplicationRequestException(String message, ErrorCode errorCode) {
        super(message != null ? message : errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return super.getMessage(); // 부모 클래스의 getMessage() 사용
    }
}
