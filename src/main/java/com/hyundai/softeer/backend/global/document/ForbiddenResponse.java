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
@ApiResponse(responseCode = "403", description = "접근 권한이 없을 경우 발생합니다.")
public @interface ForbiddenResponse {
    Content[] content() default {
            @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(value = "{\n  \"status\": \"FORBIDDEN\",\n  \"message\": \"접근 권한이 없습니다.\"\n}")
            )
    };
}