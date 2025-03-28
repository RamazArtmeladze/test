package com.andersenlab.netto.prohealth.financialManagementService.util;


import com.andersenlab.netto.prohealth.financialManagementService.exception.HealthcareProviderIdValidationException;
import lombok.experimental.UtilityClass;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;
import java.util.UUID;

import static com.andersenlab.netto.prohealth.financialManagementService.util.MessageConstants.HEALTHCARE_PROVIDER_ID_PARSING_FAIL_MESSAGE;
import static com.andersenlab.netto.prohealth.financialManagementService.util.MessageConstants.HEALTHCARE_PROVIDER_ID_VALIDATION_FAIL_MESSAGE;


@UtilityClass
public class JwtUtil {

    public static UUID getIdFromJwtToken(Jwt jwt) {
        return Optional.ofNullable(jwt.getClaims().get("sub"))
                .map(Object::toString)
                .map(JwtUtil::safeParseUUID)
                .orElseThrow(() -> new HealthcareProviderIdValidationException(HEALTHCARE_PROVIDER_ID_VALIDATION_FAIL_MESSAGE));
    }

    private static UUID safeParseUUID(String uuidString) {
        try {
            return UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            throw new HealthcareProviderIdValidationException(HEALTHCARE_PROVIDER_ID_PARSING_FAIL_MESSAGE);
        }
    }
}