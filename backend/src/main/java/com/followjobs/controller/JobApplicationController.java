package com.followjobs.controller;

import com.followjobs.dto.JobApplicationDTO;
import com.followjobs.dto.UpdateStatusDTO;
import com.followjobs.entity.ApplicationStatus;
import com.followjobs.service.JobApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for job applications.
 * Handles HTTP requests and delegates to the service layer.
 */
@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Job Applications", description = "API for managing job applications")
@CrossOrigin(origins = "*")
public class JobApplicationController {

    private final JobApplicationService service;

    // ---------- GET Endpoints ----------

    @GetMapping
    @Operation(summary = "Get all applications", description = "Returns all applications ordered by date")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    })
    public ResponseEntity<List<JobApplicationDTO>> getAll() {
        log.info("GET /api/applications");
        List<JobApplicationDTO> applications = service.findAll();
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get application by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application found"),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    public ResponseEntity<JobApplicationDTO> getById(
            @Parameter(description = "Application ID") @PathVariable Long id) {

        log.info("GET /api/applications/{}", id);

        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/portal/{portal}")
    @Operation(summary = "Search by portal", description = "Filter applications by portal (LinkedIn, Indeed, etc.)")
    public ResponseEntity<List<JobApplicationDTO>> getByPortal(
            @Parameter(description = "Portal name: LinkedIn, Indeed, Computrabajo, ChileTrabajos") @PathVariable String portal) {

        log.info("GET /api/applications/portal/{}", portal);
        return ResponseEntity.ok(service.findByPortal(portal));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Search by status")
    public ResponseEntity<List<JobApplicationDTO>> getByStatus(
            @Parameter(description = "Status: SENT, REJECTED, ACCEPTED, INTERVIEW") @PathVariable ApplicationStatus status) {

        log.info("GET /api/applications/status/{}", status);
        return ResponseEntity.ok(service.findByStatus(status));
    }

    @GetMapping("/search")
    @Operation(summary = "Search by company", description = "Partial search by company name")
    public ResponseEntity<List<JobApplicationDTO>> searchByCompany(
            @Parameter(description = "Text to search in company name") @RequestParam String company) {

        log.info("GET /api/applications/search?company={}", company);
        return ResponseEntity.ok(service.searchByCompany(company));
    }

    // ---------- POST Endpoints ----------

    @PostMapping
    @Operation(summary = "Create new application")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Application created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    public ResponseEntity<JobApplicationDTO> create(
            @Valid @RequestBody JobApplicationDTO dto) {

        log.info("POST /api/applications - Creating: {} at {}", dto.getPosition(), dto.getCompany());
        JobApplicationDTO created = service.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ---------- PUT/PATCH Endpoints ----------

    @PutMapping("/{id}")
    @Operation(summary = "Update application")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Application updated"),
            @ApiResponse(responseCode = "404", description = "Application not found"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    public ResponseEntity<JobApplicationDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody JobApplicationDTO dto) {

        log.info("PUT /api/applications/{}", id);

        return service.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update status", description = "Updates status and automatically records response date")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated"),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    public ResponseEntity<JobApplicationDTO> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusDTO dto) {

        log.info("PATCH /api/applications/{}/status - New status: {}", id, dto.getStatus());

        return service.updateStatus(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ---------- DELETE Endpoints ----------

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete application")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Application deleted"),
            @ApiResponse(responseCode = "404", description = "Application not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/applications/{}", id);

        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ---------- Special Operations ----------

    @PostMapping("/clean")
    @Operation(summary = "Clean invalid data", description = "Removes applications with empty or invalid company/position")
    public ResponseEntity<String> cleanInvalidApplications() {
        log.info("POST /api/applications/clean");
        int deleted = service.cleanInvalidApplications();
        return ResponseEntity.ok("Applications deleted: " + deleted);
    }
}
