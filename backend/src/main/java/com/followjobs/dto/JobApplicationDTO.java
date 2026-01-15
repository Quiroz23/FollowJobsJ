package com.followjobs.dto;

import com.followjobs.entity.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO for transferring job application data through the API.
 * Separates the external API representation from the internal entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationDTO {

    private Long id;

    private LocalDateTime applicationDate;

    @NotBlank(message = "Company name is required")
    @Size(max = 255, message = "Company name cannot exceed 255 characters")
    private String company;

    @NotBlank(message = "Position is required")
    @Size(max = 255, message = "Position cannot exceed 255 characters")
    private String position;

    @Size(max = 100, message = "Employment type cannot exceed 100 characters")
    private String employmentType;

    @NotBlank(message = "Portal is required")
    @Size(max = 50, message = "Portal cannot exceed 50 characters")
    private String portal;

    @NotNull(message = "Status is required")
    private ApplicationStatus status;

    private LocalDateTime responseDate;

    @Size(max = 500, message = "URL cannot exceed 500 characters")
    private String jobUrl;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
