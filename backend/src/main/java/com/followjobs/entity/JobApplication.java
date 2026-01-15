package com.followjobs.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entity representing a job application.
 * Maps to the job_applications table in the database.
 */
@Entity
@Table(name = "job_applications")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_date", nullable = false)
    private LocalDateTime applicationDate;

    @Column(name = "company", nullable = false, length = 255)
    private String company;

    @Column(name = "position", nullable = false, length = 255)
    private String position;

    /** Employment type: Remote, On-site, Hybrid, Full-time, Part-time, etc. */
    @Column(name = "employment_type", length = 100)
    private String employmentType;

    /** Source portal: LinkedIn, Indeed, Computrabajo, ChileTrabajos */
    @Column(name = "portal", nullable = false, length = 50)
    private String portal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ApplicationStatus status;

    /** Date when the company responded (null if no response yet) */
    @Column(name = "response_date")
    private LocalDateTime responseDate;

    @Column(name = "job_url", length = 500)
    private String jobUrl;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /** Gmail message ID to prevent duplicate processing */
    @Column(name = "gmail_message_id", unique = true, length = 100)
    private String gmailMessageId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = ApplicationStatus.SENT;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
