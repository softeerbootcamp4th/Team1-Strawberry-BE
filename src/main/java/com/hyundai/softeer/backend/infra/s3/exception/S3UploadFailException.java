package com.hyundai.softeer.backend.infra.s3.exception;

import com.hyundai.softeer.backend.global.exception.BaseException;
import org.springframework.http.HttpStatus;

public class S3UploadFailException extends BaseException {
    public S3UploadFailException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "S3 업로드에 실패했습니다.");
    }
}
