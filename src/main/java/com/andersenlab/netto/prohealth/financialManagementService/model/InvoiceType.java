package com.andersenlab.netto.prohealth.financialManagementService.model;

public enum InvoiceType {
    INSURANCE("Insurance"),
    PERSONAL_PAYMENT("Personal payment");

    private final String description;
    InvoiceType(String description) {
        this.description = description;
    }
}
