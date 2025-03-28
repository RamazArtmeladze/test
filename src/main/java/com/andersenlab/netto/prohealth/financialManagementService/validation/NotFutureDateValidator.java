package com.andersenlab.netto.prohealth.financialManagementService.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class NotFutureDateValidator implements ConstraintValidator<NotFutureDate, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        return value == null || !value.isAfter(LocalDateTime.now());
    }
}

