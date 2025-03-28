package com.andersenlab.netto.prohealth.financialManagementService.service;


import com.andersenlab.netto.prohealth.financialManagementService.dto.CreateInvoiceDto;
import com.andersenlab.netto.prohealth.financialManagementService.dto.EditInvoiceDto;
import com.andersenlab.netto.prohealth.financialManagementService.dto.InvoiceModelDto;
import com.andersenlab.netto.prohealth.financialManagementService.feign.HealthcareProviderClient;
import com.andersenlab.netto.prohealth.financialManagementService.mapper.InvoiceModelMapper;
import com.andersenlab.netto.prohealth.financialManagementService.model.Invoice;
import com.andersenlab.netto.prohealth.financialManagementService.model.PaymentStatus;
import com.andersenlab.netto.prohealth.financialManagementService.repository.InvoiceRepository;
import com.andersenlab.netto.prohealth.financialManagementService.service.impl.InvoiceServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.andersenlab.netto.prohealth.financialManagementService.model.InvoiceType.PERSONAL_PAYMENT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @InjectMocks
    private InvoiceServiceImpl invoiceServiceImpl;
    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private HealthcareProviderClient healthcareProviderClient;
    @Mock
    private CreateInvoiceDto createInvoiceDto;
    @Mock
    private InvoiceModelMapper mapper;

    private EditInvoiceDto editInvoiceDto;
    private Invoice invoice;
    InvoiceModelDto invoiceModelDto;

    UUID creatorId = UUID.randomUUID();
    UUID patientId = UUID.randomUUID();
    UUID invoiceId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        createInvoiceDto = new CreateInvoiceDto(
                "Ramaz Art",
                patientId,
                "test@email.com",
                PERSONAL_PAYMENT,
                LocalDateTime.now(),
                BigDecimal.valueOf (100.00),
                "healthcare service",
                "credit/debit card",
                null,
                "some comments"
        );

        editInvoiceDto = new EditInvoiceDto(
                invoiceId,
                LocalDateTime.now(),
                "updated service",
                BigDecimal.valueOf( 43100)
        );

        invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setCreatorId(creatorId);
        invoice.setPatientName(createInvoiceDto.patientName());
        invoice.setPatientId(patientId);
        invoice.setPatientEmail(createInvoiceDto.patientEmail());
        invoice.setInvoiceType(createInvoiceDto.invoiceType());
        invoice.setInvoiceDate(createInvoiceDto.invoiceDate());
        invoice.setInvoiceAmount(createInvoiceDto.invoiceAmount());
        invoice.setServiceProvided(createInvoiceDto.serviceProvided());
        invoice.setPaymentMethod(createInvoiceDto.paymentMethod());
        invoice.setComment(createInvoiceDto.comment());

        invoiceModelDto = new InvoiceModelDto(
                invoiceId,
                creatorId,
                "INV-20250205-185059",
                createInvoiceDto.patientName(),
                patientId,
                createInvoiceDto.patientEmail(),
                PERSONAL_PAYMENT,
                LocalDateTime.now(),
                LocalDateTime.now(),
                BigDecimal.valueOf(100.00),
                "healthcare service",
                null,
                createInvoiceDto.paymentMethod(),
                PaymentStatus.UNPAID,
                false,
                createInvoiceDto.comment()
        );
    }

    @Test
    void createInvoiceSuccessfully() {
        when(healthcareProviderClient.existsById(creatorId))
                .thenReturn( ResponseEntity.ok(Boolean.TRUE));

        when(mapper.toEntity(createInvoiceDto)).thenReturn(invoice);
        when(invoiceRepository.save(invoice)).thenReturn(invoice);
        when(mapper.toDto(invoice)).thenReturn(invoiceModelDto);

        InvoiceModelDto result = invoiceServiceImpl.createInvoice(createInvoiceDto, creatorId);

        assertNotNull(result);
        assertEquals(invoiceModelDto, result);
    }

    @Test
    void createInvoiceWhenHealthcareProviderDoesNotExistThenThrowEntityNotFoundException() {
        when(healthcareProviderClient.existsById(creatorId))
                .thenReturn( ResponseEntity.ok(Boolean.FALSE));

        assertThrows(EntityNotFoundException.class, () ->
                invoiceServiceImpl.createInvoice(createInvoiceDto, creatorId));
    }

    @Test
    void editInvoiceSuccessfully() {
        when(healthcareProviderClient.existsById(creatorId))
                .thenReturn( ResponseEntity.ok(Boolean.TRUE));

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(invoice));

        invoice.setInvoiceAmount(editInvoiceDto.invoiceAmount());
        invoice.setServiceProvided(editInvoiceDto.serviceProvided());
        invoice.setInvoiceDate(editInvoiceDto.invoiceDate());

        when(invoiceRepository.save(invoice)).thenReturn(invoice);
        when(mapper.toDto(invoice)).thenReturn(invoiceModelDto);

        InvoiceModelDto result = invoiceServiceImpl.editInvoice(editInvoiceDto, creatorId);

        assertNotNull(result);
        assertEquals(invoiceModelDto, result);
    }

    @Test
    void editNonExistentInvoiceThenThrowEntityNotFoundException() {
        when(healthcareProviderClient.existsById(creatorId))
                .thenReturn( ResponseEntity.ok(Boolean.TRUE));

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                invoiceServiceImpl.editInvoice(editInvoiceDto, creatorId));
    }
}
