package com.followjobs.dto;

/**
 * Record for application statistics.
 * Uses Java 17+ record feature for immutable data.
 */
public record ApplicationStatsDTO(
        long total,
        long sent,
        long rejected,
        long accepted,
        long interviews) {
    /** Calculate success rate (accepted + interviews) */
    public double successRate() {
        if (total == 0)
            return 0.0;
        return ((double) (accepted + interviews) / total) * 100;
    }

    public double rejectionRate() {
        if (total == 0)
            return 0.0;
        return ((double) rejected / total) * 100;
    }
}
