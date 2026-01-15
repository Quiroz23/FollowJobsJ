package com.followjobs.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration.
 * Customizes the API documentation visible at /swagger-ui.html
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FollowJobs API")
                        .version("1.0.0")
                        .description("""
                                REST API for tracking job applications.

                                ## Features
                                - CRUD operations for job applications
                                - Search by portal, status, company
                                - Gmail sync (coming soon)
                                - Statistics (coming soon)

                                ## Supported Portals
                                - LinkedIn
                                - Indeed
                                - Computrabajo
                                - ChileTrabajos
                                """)
                        .contact(new Contact()
                                .name("FollowJobs")
                                .email("contact@example.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
