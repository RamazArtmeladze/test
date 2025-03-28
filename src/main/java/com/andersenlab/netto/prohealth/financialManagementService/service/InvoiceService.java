package com.andersenlab.netto.prohealth.financialManagementService.service;

import com.andersenlab.netto.prohealth.financialManagementService.dto.CreateInvoiceDto;
import com.andersenlab.netto.prohealth.financialManagementService.dto.EditInvoiceDto;
import com.andersenlab.netto.prohealth.financialManagementService.dto.InvoiceModelDto;

import java.util.UUID;

public interface InvoiceService {
    InvoiceModelDto createInvoice  (CreateInvoiceDto createInvoiceDto, UUID creatorId );

    InvoiceModelDto editInvoice (EditInvoiceDto editInvoiceDto, UUID creatorId );
}
