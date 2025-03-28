package com.andersenlab.netto.prohealth.financialManagementService.model;

public enum PaymentStatus {
    PAID("paid"),
    UNPAID("unpaid"),
    PENDING_APPROVAL("pending approval");

    private final String description;

    PaymentStatus (String description) {
        this.description = description;
    }
}
