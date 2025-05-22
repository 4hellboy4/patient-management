package com.yoshatech.patientservice.contoller;

import com.yoshatech.patientservice.dto.PatientRequestDto;
import com.yoshatech.patientservice.dto.PatientResponseDto;
import com.yoshatech.patientservice.dto.validators.CreatePatientValidationGroup;
import com.yoshatech.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger. v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
@Tag( name = "Patient", description = "API for managing patients")
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/get-patients")
    @Operation(summary = "Get all patients", description = "Retrieve a list of all patients")
    public ResponseEntity<List<PatientResponseDto>> getPatients() {
        log.info("Getting all the Patients");
        List<PatientResponseDto> patients = patientService.getPatients();

        return ResponseEntity.ok().body(patients );
    }

    @PostMapping("/create-patient")
    @Operation(summary = "Create a new patient", description = "Create a new patient with the provided details")
    @Transactional
    public ResponseEntity<PatientResponseDto> createPatient(@Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDto patientRequestDto) {
        log.info("Creating new patient with email: {}", patientRequestDto.getEmail());

        PatientResponseDto createdPatient = patientService.createPatient(patientRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Updating the patient with the given ID", description = "Update the details of an existing patient")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable UUID id, @Validated({Default.class}) @RequestBody PatientRequestDto patientRequestDto) {
        log.info("Updating patient with id: {}", id);

        PatientResponseDto updatedPatient = patientService.updatePatient(id, patientRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(updatedPatient);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleting the patient with the given ID")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        log.info("Deleting patient with id: {}", id);

        patientService.deletePatient(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
