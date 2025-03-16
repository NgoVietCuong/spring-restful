package com.nvc.spring_boot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme bearerAuthScheme =
                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .info(
                        new Info()
                                .title("API Documentation")
                                .version("1.0")
                                .description("API with Bearer Token Authentication"))
                .addServersItem(new Server().url(System.getenv("GATEWAY_URL")))
                .addSecurityItem(securityRequirement)
                .schemaRequirement("bearerAuth", bearerAuthScheme);
    }
}
