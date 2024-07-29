package com.hyundai.softeer.backend.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${version}")
    private String version;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().addServersItem(new Server().url("/"))
                .components(new Components().addSecuritySchemes("access-token",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .info(apiInfo())
                .tags(tagList());
    }

    private Info apiInfo() {
        return new Info()
                .title("현대 소프티어 부트캠프 4기 - 1팀 Backend API")
                .description("현대 소프티어 부트캠프 4기 1팀의 Backend API를 확인할 수 있습니다.")
                .version(version);
    }

    private List<Tag> tagList() {
        return List.of(
                new Tag().name("User").description("사용자 관련 API")
        );
    }
}
