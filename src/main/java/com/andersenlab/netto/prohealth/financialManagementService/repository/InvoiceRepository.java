package com.andersenlab.netto.prohealth.financialManagementService.repository;

import com.andersenlab.netto.prohealth.financialManagementService.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
}
