package com.followjobs.entity;

/**
 * Possible states for a job application.
 * Each status has a display name in Spanish for the UI.
 */
public enum ApplicationStatus {

    SENT("Enviado"),
    REJECTED("Rechazado"),
    ACCEPTED("Aceptado"),
    INTERVIEW("Entrevista"),
    NO_RESPONSE("Sin respuesta");

    private final String displayName;

    ApplicationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
