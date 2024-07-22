package com.hyundai.softeer.backend.global.document;

import com.hyundai.softeer.backend.global.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;

import java.lang.annotation.*;


@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "401", description = "엑세스 토큰이 유효하지 않을 경우 발생합니다.")
public @interface InvalidTokenResponse {
    Content[] content() default {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(value = "{\n  \"status\": \"UNAUTHORIZED\",\n  \"message\": \"유효하지 않은 토큰입니다.\"\n}")
            )
    };
}