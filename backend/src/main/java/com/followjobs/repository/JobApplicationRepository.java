package com.followjobs.repository;

import com.followjobs.entity.ApplicationStatus;
import com.followjobs.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for JobApplication entity.
 * Spring Data JPA generates the implementation automatically.
 */
@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByPortal(String portal);

    List<JobApplication> findByStatus(ApplicationStatus status);

    List<JobApplication> findByCompanyContainingIgnoreCase(String company);

    List<JobApplication> findByPositionContainingIgnoreCase(String position);

    List<JobApplication> findByPortalAndStatusOrderByApplicationDateDesc(
            String portal,
            ApplicationStatus status);

    Optional<JobApplication> findByGmailMessageId(String gmailMessageId);

    boolean existsByGmailMessageId(String gmailMessageId);

    List<JobApplication> findByApplicationDateBetween(
            LocalDateTime startDate,
            LocalDateTime endDate);

    @Query("SELECT COUNT(j) FROM JobApplication j WHERE j.status = :status")
    long countByStatus(@Param("status") ApplicationStatus status);

    @Query("SELECT j FROM JobApplication j ORDER BY j.applicationDate DESC")
    List<JobApplication> findAllOrderByDateDesc();

    /** Find applications without response after a certain date */
    @Query("""
                SELECT j FROM JobApplication j
                WHERE j.status = 'SENT'
                AND j.applicationDate < :cutoffDate
                AND j.responseDate IS NULL
                ORDER BY j.applicationDate ASC
            """)
    List<JobApplication> findStaleApplications(@Param("cutoffDate") LocalDateTime cutoffDate);

    @Query("SELECT j.portal, COUNT(j) FROM JobApplication j GROUP BY j.portal")
    List<Object[]> countByPortalGrouped();

    @Query("SELECT j.status, COUNT(j) FROM JobApplication j GROUP BY j.status")
    List<Object[]> countByStatusGrouped();

    /** Delete applications with invalid company/position data */
    @org.springframework.data.jpa.repository.Modifying
    @Query("""
                DELETE FROM JobApplication j
                WHERE (j.company = 'No encontrado' OR j.company = '')
                AND (j.position = 'No encontrado' OR j.position = '')
            """)
    int deleteInvalidApplications();
}
