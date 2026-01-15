package com.followjobs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the FollowJobs application.
 * 
 * This Spring Boot app helps track job applications across different portals
 * like LinkedIn, Indeed, Computrabajo, etc.
 */
@SpringBootApplication
public class FollowJobsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FollowJobsApplication.class, args);

        // Once running, you can access:
        // - API: http://localhost:8080/api/applications
        // - Swagger UI: http://localhost:8080/swagger-ui.html
        // - H2 Console: http://localhost:8080/h2-console
    }
}
