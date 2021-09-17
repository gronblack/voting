package ru.topjava.voting.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//https://sabljakovich.medium.com/adding-basic-auth-authorization-option-to-openapi-swagger-documentation-java-spring-95abbede27e9
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@OpenAPIDefinition(
        info = @Info(
                title = "REST API documentation",
                version = "1.0",
                description = "<p>Restaurant voting: <a href='https://javaops.ru/view/topjava'>TopJava</a> graduation project</p>" +
                        "<p>" +
                        "<b>Credentials:</b><br>" +
                        "<b>Admin</b>: <span>admin@voting.ru:admin</span><br>" +
                        "<b>User 1</b>: <span>user@gmail.com:password</span><br>" +
                        "<b>User 2</b>: <span>user2@gmail.com:password2</span>" +
                        "</p>"
        ),
        security = @SecurityRequirement(name = "basicAuth")
)
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("REST API")
                .pathsToMatch("/api/**")
                .build();
    }
}

