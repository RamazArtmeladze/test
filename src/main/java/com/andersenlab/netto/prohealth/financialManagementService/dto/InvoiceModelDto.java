package com.andersenlab.netto.prohealth.financialManagementService.dto;

import com.andersenlab.netto.prohealth.financialManagementService.model.InvoiceType;
import com.andersenlab.netto.prohealth.financialManagementService.model.PaymentMethod;
import com.andersenlab.netto.prohealth.financialManagementService.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record InvoiceModelDto(
        UUID id,
        UUID creatorId,
        String invoiceNumber,
        String patientName,
        UUID patientId,
        String patientEmail,
        InvoiceType invoiceType,
        LocalDateTime invoiceDate,
        LocalDateTime creationTime,
        BigDecimal invoiceAmount,
        String serviceProvided,
        String insurancePolicyNumber,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        Boolean isSent,
        String comment
) {
}
