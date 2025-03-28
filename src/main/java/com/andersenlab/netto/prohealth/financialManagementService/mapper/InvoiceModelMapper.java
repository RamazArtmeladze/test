package com.andersenlab.netto.prohealth.financialManagementService.mapper;

import com.andersenlab.netto.prohealth.financialManagementService.dto.CreateInvoiceDto;
import com.andersenlab.netto.prohealth.financialManagementService.dto.InvoiceModelDto;
import com.andersenlab.netto.prohealth.financialManagementService.model.Invoice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface InvoiceModelMapper {

    @Named("currentDateTime")
    default LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }

    @Named("defaultIsSent")
    default boolean defaultIsSent() {
        return false;
    }

    @Named("generateInvoiceNumber")
    default String generateInvoiceNumber() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
        return "INV-" + LocalDateTime.now().format(formatter);
    }

    @Mapping(target = "creationTime", expression = "java(currentDateTime())")
    @Mapping(target = "isSent", expression = "java(defaultIsSent())")
    @Mapping(target = "invoiceNumber", expression = "java(generateInvoiceNumber())")
    Invoice toEntity (CreateInvoiceDto createInvoiceDto);

    InvoiceModelDto toDto (Invoice invoice);
}
