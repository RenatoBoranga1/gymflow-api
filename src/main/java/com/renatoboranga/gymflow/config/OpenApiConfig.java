package com.renatoboranga.gymflow.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI gymFlowOpenApi() {
        return new OpenAPI().info(new Info()
                .title("GymFlow API")
                .description("API REST para clientes, professores, planos e treinos de academias.")
                .version("v1")
                .contact(new Contact().name("Renato Boranga"))
                .license(new License().name("MIT")));
    }
}
