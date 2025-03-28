package com.andersenlab.netto.prohealth.financialManagementService.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class MessageConstants {

    public static final String FAILED_TO_CHECK_HEALTHCARE_PROVIDER = "Failed to check existence of a healthcare provider with the given id due to feign client error";
    public static final String HEALTHCARE_PROVIDER_ID_VALIDATION_FAIL_MESSAGE = "Healthcare provider's id is missing or is invalid";
    public static final String HEALTHCARE_PROVIDER_ID_PARSING_FAIL_MESSAGE = "Can't parse a valid id from this token";
    public static final String HEALTHCARE_PROVIDER_NOT_FOUND_MESSAGE = "No healthcare provider with the given id is found";
    public static final String INVOICE_NOT_FOUND_MESSAGE = "Invoice not found";
    public static final String TWO_FACTOR_AUTHENTICATION_NOT_COMPLETE = "Please complete two-factor authentication";
}
