package com.followjobs.service;

import com.followjobs.dto.JobApplicationDTO;
import com.followjobs.dto.UpdateStatusDTO;
import com.followjobs.entity.ApplicationStatus;
import com.followjobs.entity.JobApplication;
import com.followjobs.repository.JobApplicationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service layer for job applications.
 * Contains business logic and acts as intermediary between controller and
 * repository.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JobApplicationService {

    private final JobApplicationRepository repository;

    // ---------- CRUD Operations ----------

    public List<JobApplicationDTO> findAll() {
        log.info("Fetching all applications");
        return repository.findAllOrderByDateDesc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<JobApplicationDTO> findById(Long id) {
        log.info("Finding application with ID: {}", id);
        return repository.findById(id).map(this::toDTO);
    }

    @Transactional
    public JobApplicationDTO create(JobApplicationDTO dto) {
        log.info("Creating application: {} - {}", dto.getCompany(), dto.getPosition());

        JobApplication entity = toEntity(dto);
        entity.setApplicationDate(
                dto.getApplicationDate() != null ? dto.getApplicationDate() : LocalDateTime.now());
        entity.setStatus(ApplicationStatus.SENT);

        JobApplication saved = repository.save(entity);
        log.info("Application created with ID: {}", saved.getId());

        return toDTO(saved);
    }

    @Transactional
    public Optional<JobApplicationDTO> update(Long id, JobApplicationDTO dto) {
        log.info("Updating application with ID: {}", id);

        return repository.findById(id).map(existing -> {
            existing.setCompany(dto.getCompany());
            existing.setPosition(dto.getPosition());
            existing.setEmploymentType(dto.getEmploymentType());
            existing.setPortal(dto.getPortal());
            existing.setJobUrl(dto.getJobUrl());
            existing.setNotes(dto.getNotes());

            if (dto.getStatus() != null) {
                existing.setStatus(dto.getStatus());
            }

            JobApplication updated = repository.save(existing);
            log.info("Application updated: {}", updated.getId());
            return toDTO(updated);
        });
    }

    /**
     * Updates only the status of an application.
     * Automatically records response date for REJECTED, ACCEPTED, or INTERVIEW.
     */
    @Transactional
    public Optional<JobApplicationDTO> updateStatus(Long id, UpdateStatusDTO dto) {
        log.info("Updating status of application {} to {}", id, dto.getStatus());

        return repository.findById(id).map(existing -> {
            ApplicationStatus newStatus = dto.getStatus();
            existing.setStatus(newStatus);

            // Record response date when company responds
            if (newStatus == ApplicationStatus.REJECTED ||
                    newStatus == ApplicationStatus.ACCEPTED ||
                    newStatus == ApplicationStatus.INTERVIEW) {

                existing.setResponseDate(LocalDateTime.now());
                log.info("Response date recorded for application {}", id);
            }

            // Append notes if provided
            if (dto.getNotes() != null && !dto.getNotes().isBlank()) {
                String currentNotes = existing.getNotes() != null ? existing.getNotes() + "\n" : "";
                existing.setNotes(currentNotes + "[" + LocalDateTime.now() + "] " + dto.getNotes());
            }

            return toDTO(repository.save(existing));
        });
    }

    @Transactional
    public boolean delete(Long id) {
        log.info("Deleting application with ID: {}", id);

        if (repository.existsById(id)) {
            repository.deleteById(id);
            log.info("Application deleted: {}", id);
            return true;
        }

        log.warn("Application not found for deletion: {}", id);
        return false;
    }

    // ---------- Search Operations ----------

    public List<JobApplicationDTO> findByPortal(String portal) {
        log.info("Finding applications from portal: {}", portal);
        return repository.findByPortal(portal)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<JobApplicationDTO> findByStatus(ApplicationStatus status) {
        log.info("Finding applications with status: {}", status);
        return repository.findByStatus(status)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<JobApplicationDTO> searchByCompany(String company) {
        log.info("Searching applications by company: {}", company);
        return repository.findByCompanyContainingIgnoreCase(company)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ---------- Cleanup Operations ----------

    /** Removes applications with invalid data (empty company/position) */
    @Transactional
    public int cleanInvalidApplications() {
        log.info("Running cleanup of invalid applications");
        int deleted = repository.deleteInvalidApplications();
        log.info("Invalid applications deleted: {}", deleted);
        return deleted;
    }

    // ---------- Mappers ----------

    private JobApplicationDTO toDTO(JobApplication entity) {
        return JobApplicationDTO.builder()
                .id(entity.getId())
                .applicationDate(entity.getApplicationDate())
                .company(entity.getCompany())
                .position(entity.getPosition())
                .employmentType(entity.getEmploymentType())
                .portal(entity.getPortal())
                .status(entity.getStatus())
                .responseDate(entity.getResponseDate())
                .jobUrl(entity.getJobUrl())
                .notes(entity.getNotes())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private JobApplication toEntity(JobApplicationDTO dto) {
        return JobApplication.builder()
                .applicationDate(dto.getApplicationDate())
                .company(dto.getCompany())
                .position(dto.getPosition())
                .employmentType(dto.getEmploymentType())
                .portal(dto.getPortal())
                .status(dto.getStatus())
                .responseDate(dto.getResponseDate())
                .jobUrl(dto.getJobUrl())
                .notes(dto.getNotes())
                .build();
    }
}
