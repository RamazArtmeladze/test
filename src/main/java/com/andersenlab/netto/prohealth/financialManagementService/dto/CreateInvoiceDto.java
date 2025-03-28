package com.andersenlab.netto.prohealth.financialManagementService.dto;

import com.andersenlab.netto.prohealth.financialManagementService.model.InvoiceType;
import com.andersenlab.netto.prohealth.financialManagementService.model.PaymentMethod;
import com.andersenlab.netto.prohealth.financialManagementService.validation.NotFutureDate;
import io.smallrye.common.constraint.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateInvoiceDto(
        @NotNull String patientName,
        @NotNull UUID patientId,
        @NotNull String patientEmail,
        @NotNull InvoiceType invoiceType,
        @NotFutureDate @NotNull LocalDateTime invoiceDate,
        @NotNull BigDecimal invoiceAmount,
        @NotBlank String serviceProvided,
        @NotBlank String insurancePolicyNumber,
        @NotNull PaymentMethod paymentMethod,
        String comment
) { }