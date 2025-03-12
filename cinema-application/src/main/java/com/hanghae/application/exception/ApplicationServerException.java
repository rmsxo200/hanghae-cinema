package com.hanghae.application.exception;

import com.hanghae.application.enums.ErrorCode;
import lombok.Getter;

/**
 * 서버 내부 오류 Exception
 * 500 응답위해 Exception 구분
 */
@Getter
public class ApplicationServerException extends RuntimeException {
    private final ErrorCode errorCode;

    public ApplicationServerException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ApplicationServerException(String message, ErrorCode errorCode) {
        super(message != null ? message : errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return super.getMessage(); // 부모 클래스의 getMessage() 사용
    }
}
