package com.andersenlab.netto.prohealth.financialManagementService.service.impl;

import com.andersenlab.netto.prohealth.financialManagementService.dto.CreateInvoiceDto;
import com.andersenlab.netto.prohealth.financialManagementService.dto.EditInvoiceDto;
import com.andersenlab.netto.prohealth.financialManagementService.dto.InvoiceModelDto;
import com.andersenlab.netto.prohealth.financialManagementService.exception.HealthcareProviderCheckingException;
import com.andersenlab.netto.prohealth.financialManagementService.feign.HealthcareProviderClient;
import com.andersenlab.netto.prohealth.financialManagementService.mapper.InvoiceModelMapper;
import com.andersenlab.netto.prohealth.financialManagementService.model.Invoice;
import com.andersenlab.netto.prohealth.financialManagementService.model.InvoiceType;
import com.andersenlab.netto.prohealth.financialManagementService.repository.InvoiceRepository;
import com.andersenlab.netto.prohealth.financialManagementService.service.InvoiceService;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.andersenlab.netto.prohealth.financialManagementService.model.PaymentStatus.PENDING_APPROVAL;
import static com.andersenlab.netto.prohealth.financialManagementService.model.PaymentStatus.UNPAID;
import static com.andersenlab.netto.prohealth.financialManagementService.util.MessageConstants.FAILED_TO_CHECK_HEALTHCARE_PROVIDER;
import static com.andersenlab.netto.prohealth.financialManagementService.util.MessageConstants.HEALTHCARE_PROVIDER_NOT_FOUND_MESSAGE;
import static com.andersenlab.netto.prohealth.financialManagementService.util.MessageConstants.INVOICE_NOT_FOUND_MESSAGE;

@Service
@AllArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final HealthcareProviderClient healthcareProviderClient;
    private final InvoiceModelMapper mapper;
    private static final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    public InvoiceModelDto createInvoice  (CreateInvoiceDto createInvoiceDto, UUID creatorId ) {

        if (!checkHealthcareProviderExists(creatorId)) {
            throw new EntityNotFoundException(HEALTHCARE_PROVIDER_NOT_FOUND_MESSAGE);
        }

        Invoice invoiceModel = mapper.toEntity(createInvoiceDto);
        invoiceModel.setCreatorId(creatorId);

        if (createInvoiceDto.invoiceType() == InvoiceType.INSURANCE) {
            invoiceModel.setPaymentMethod(null);
            invoiceModel.setPaymentStatus(PENDING_APPROVAL);
        } else if (createInvoiceDto.invoiceType() == InvoiceType.PERSONAL_PAYMENT) {
            invoiceModel.setInsurancePolicyNumber(null);
            invoiceModel.setPaymentStatus(UNPAID);
        }

        Invoice savedInvoice = invoiceRepository.save(invoiceModel);

        return mapper.toDto(savedInvoice);
    }

    public InvoiceModelDto editInvoice (EditInvoiceDto editInvoiceDto, UUID creatorId ) {

        if (!checkHealthcareProviderExists(creatorId)) {
            throw new EntityNotFoundException(HEALTHCARE_PROVIDER_NOT_FOUND_MESSAGE);
        }

        Invoice updatedInvoice = invoiceRepository.save(updateInvoiceFields(editInvoiceDto));

        return mapper.toDto(updatedInvoice);
    }

    private Invoice updateInvoiceFields(EditInvoiceDto editInvoiceDto) {

        Invoice existingInvoice = invoiceRepository.findById(editInvoiceDto.invoiceId())
                .orElseThrow(() -> new EntityNotFoundException(INVOICE_NOT_FOUND_MESSAGE));

        if (editInvoiceDto.invoiceDate() != null) {
            existingInvoice.setInvoiceDate(editInvoiceDto.invoiceDate());
        }

        if (editInvoiceDto.serviceProvided() != null) {
            existingInvoice.setServiceProvided(editInvoiceDto.serviceProvided());
        }
        if (editInvoiceDto.invoiceAmount() != null) {
            existingInvoice.setInvoiceAmount(editInvoiceDto.invoiceAmount());
        }

        return existingInvoice;
    }

    private boolean checkHealthcareProviderExists(UUID creatorId) {
        try {
            return Optional.ofNullable(healthcareProviderClient.existsById(creatorId))
                    .map(ResponseEntity::getBody)
                    .orElseThrow(() -> new HealthcareProviderCheckingException(FAILED_TO_CHECK_HEALTHCARE_PROVIDER));
        } catch (FeignException.NotFound ex) {
            return false;
        } catch (FeignException ex) {
            log.warn("Failed to check healthcare provider with ID {}: {}", creatorId, ex.getMessage());
            throw new HealthcareProviderCheckingException(FAILED_TO_CHECK_HEALTHCARE_PROVIDER);
        }
    }
}
