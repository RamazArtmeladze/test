package com.andersenlab.netto.prohealth.financialManagementService.model;

public enum PaymentMethod {
    CREDIT_DEBIT_CARD("credit/debit card"),
    BANK_TRANSFER("bank transfer");

    private final String description;

    PaymentMethod (String description) {
        this.description = description;
    }
}
