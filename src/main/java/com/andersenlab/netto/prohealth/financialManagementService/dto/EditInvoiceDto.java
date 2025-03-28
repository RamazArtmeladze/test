package com.andersenlab.netto.prohealth.financialManagementService.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record EditInvoiceDto(
        UUID invoiceId,
        LocalDateTime invoiceDate,
        String serviceProvided,
        BigDecimal invoiceAmount
) {
}
