package com.hyundai.softeer.backend.global.exception;

import lombok.Getter;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Getter
class SpringMvcExceptionResponse {

    public static ResponseEntity<ApiErrorResponse> of(Exception ex) {
        if (ex instanceof HttpRequestMethodNotSupportedException subEx) {
            return ApiErrorResponse.toResponseEntity(subEx.getStatusCode(), "지원하지 않는 HTTP 메소드입니다.");
        } else if (ex instanceof HttpMediaTypeNotSupportedException subEx) {
            return ApiErrorResponse.toResponseEntity(subEx.getStatusCode(), "지원하지 않는 미디어 타입입니다.");
        } else if (ex instanceof HttpMediaTypeNotAcceptableException subEx) {
            return ApiErrorResponse.toResponseEntity(subEx.getStatusCode(), "미디어 타입이 허용되지 않습니다.");
        } else if (ex instanceof MissingPathVariableException subEx) {
            return ApiErrorResponse.toResponseEntity(subEx.getStatusCode(), "Path Variable이 누락되었습니다.");
        } else if (ex instanceof MissingServletRequestParameterException subEx) {
            return ApiErrorResponse.toResponseEntity(subEx.getStatusCode(), "서블렛 요청 파라미터가 누락되었습니");
        } else if (ex instanceof MissingServletRequestPartException subEx) {
            return ApiErrorResponse.toResponseEntity(subEx.getStatusCode(), "서블렛 요청 파트가 누락되었습니");
        } else if (ex instanceof ServletRequestBindingException subEx) {
            return ApiErrorResponse.toResponseEntity(subEx.getStatusCode(), "서블렛 요청 바인딩이 실패했습니다.");
        } else if (ex instanceof MethodArgumentNotValidException subEx) {
            List<String> message = subEx.getBindingResult().getFieldErrors()
                    .stream()
                    .map(it -> "%s의 양식이 올바르지 않습니다.: %s".formatted(it.getField(), it.getDefaultMessage()))
                    .toList();
            return ApiErrorResponse.toResponseEntity(subEx.getStatusCode(), String.join("\n", message));
        } else if (ex instanceof HandlerMethodValidationException subEx) {
            return ApiErrorResponse.toResponseEntity(subEx.getStatusCode(), "메서드 유효성 검사에 실패했습니다.");
        } else if (ex instanceof NoHandlerFoundException subEx) {
            return ApiErrorResponse.toResponseEntity(subEx.getStatusCode(), "요청에 대한 핸들러를 찾을 수 없습니다.");
        } else if (ex instanceof NoResourceFoundException subEx) {
            return ApiErrorResponse.toResponseEntity(subEx.getStatusCode(), "리소스를 찾을 수 없습니다.");
        } else if (ex instanceof AsyncRequestTimeoutException subEx) {
            return ApiErrorResponse.toResponseEntity(subEx.getStatusCode(), "비동기 요청 시간 초과가 발생했습니다.");
        } else if (ex instanceof ErrorResponseException subEx) {
            return ApiErrorResponse.toResponseEntity(subEx.getStatusCode(), "에러 응답이 발생했습니다.");
        } else if (ex instanceof MaxUploadSizeExceededException subEx) {
            return ApiErrorResponse.toResponseEntity(subEx.getStatusCode(), "최대 업로드 크기를 초과했습니다.");
        } else if (ex instanceof ConversionNotSupportedException theEx) {
            return ApiErrorResponse.toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "변환 오류가 발생했습니다.");
        } else if (ex instanceof TypeMismatchException theEx) {
            return ApiErrorResponse.toResponseEntity(HttpStatus.BAD_REQUEST, "타입 불일치가 발생했습니다.");
        } else if (ex instanceof HttpMessageNotReadableException theEx) {
            return ApiErrorResponse.toResponseEntity(HttpStatus.BAD_REQUEST, "메시지를 읽을 수 없습니다.");
        } else if (ex instanceof HttpMessageNotWritableException theEx) {
            return ApiErrorResponse.toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "메시지를 쓸 수 없습니다.");
        } else if (ex instanceof MethodValidationException subEx) {
            return ApiErrorResponse.toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "메서드 유효성 검사에 실패했습니다.");
        } else if (ex instanceof BindException theEx) {
            return ApiErrorResponse.toResponseEntity(HttpStatus.BAD_REQUEST, "바인딩 오류가 발생했습니다.");
        }

        return ApiErrorResponse.toResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
    }

}
