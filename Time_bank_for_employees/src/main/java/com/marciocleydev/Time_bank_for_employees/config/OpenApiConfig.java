package com.marciocleydev.Time_bank_for_employees.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenAPI(){
        return new OpenAPI().info(new Info()
                .title("Time Bank for Employees API")
                .version("v1")
                .description("API for managing a time bank system for employees, allowing tracking of extra hours worked")
                .license(new License()
                        .name("All Rights Reserved")
                        .url("https://github.com/marciocleydev") )
        );
    }
}
