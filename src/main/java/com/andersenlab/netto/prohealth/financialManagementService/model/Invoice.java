package com.andersenlab.netto.prohealth.financialManagementService.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table (name = "invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "creator_id",nullable = false)
    private UUID creatorId; // healthcareProvider, doctor.
    @Column(name = "invoice_number",length = 50)
    private String invoiceNumber;
    @Column(name = "patient_name",length = 255)
    private String patientName;
    @Column(name = "patient_id", nullable = false)
    private UUID patientId;
    @Column(name = "patient_email", length = 50)
    private String patientEmail;
    @Enumerated(EnumType.STRING)
    @Column(name = "invoice_type", length = 50, nullable = false)
    private InvoiceType invoiceType;
    @Column(name = "invoice_date",nullable = false)
    private LocalDateTime invoiceDate;
    @Column(name = "creation_time", nullable = false)
    private LocalDateTime creationTime;
    @Column(name = "invoice_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal invoiceAmount;
    @Column(name = "service_provided", length = 500, nullable = false)
    private String serviceProvided;
    @Column(name = "insurance_policy_number", length = 20)
    private String insurancePolicyNumber;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method", length = 50)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 50)
    private PaymentStatus paymentStatus;
    @Column(name = "is_sent")
    private Boolean isSent;
    @Column(name = "comment")
    private String comment;
}