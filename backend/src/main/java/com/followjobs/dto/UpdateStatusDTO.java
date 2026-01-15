package com.followjobs.dto;

import com.followjobs.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating only the status of an application.
 * Follows Interface Segregation - clients don't need to send full application
 * data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusDTO {

    @NotNull(message = "Status is required")
    private ApplicationStatus status;

    /** Optional notes about the status change */
    private String notes;
}
