package com.andersenlab.netto.prohealth.financialManagementService.controller;

import com.andersenlab.netto.prohealth.financialManagementService.dto.CreateInvoiceDto;
import com.andersenlab.netto.prohealth.financialManagementService.dto.EditInvoiceDto;
import com.andersenlab.netto.prohealth.financialManagementService.dto.InvoiceModelDto;
import com.andersenlab.netto.prohealth.financialManagementService.service.InvoiceService;
import com.andersenlab.netto.prohealth.financialManagementService.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PreAuthorize("hasAnyAuthority('healthcare-provider', 'staff-member')")
    @PostMapping
    public ResponseEntity<InvoiceModelDto> createInvoice(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateInvoiceDto createInvoiceDto) {

        UUID creatorId = JwtUtil.getIdFromJwtToken(jwt);

        InvoiceModelDto invoice = invoiceService.createInvoice(createInvoiceDto, creatorId);

        return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
    }

    @PreAuthorize("hasAnyAuthority('healthcare-provider', 'staff-member')")
    @PatchMapping
    public ResponseEntity<InvoiceModelDto> editInvoice(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody EditInvoiceDto editInvoiceDto) {

        UUID creatorId = JwtUtil.getIdFromJwtToken(jwt);

        InvoiceModelDto invoice = invoiceService.editInvoice(editInvoiceDto, creatorId);

        return ResponseEntity.status(HttpStatus.CREATED).body(invoice);
    }
}
