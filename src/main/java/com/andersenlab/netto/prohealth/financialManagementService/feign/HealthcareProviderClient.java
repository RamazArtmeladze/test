package com.andersenlab.netto.prohealth.financialManagementService.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;


@FeignClient(name = "healthcare-provider", url = "${env.service.healthcare-provider.url}")
public interface HealthcareProviderClient {

    @GetMapping("/{id}")
    ResponseEntity<Boolean> existsById(@PathVariable("id") UUID id);
}
