package com.yoshatech.patientservice.contoller;

import com.yoshatech.patientservice.dto.PatientRequestDto;
import com.yoshatech.patientservice.dto.PatientResponseDto;
import com.yoshatech.patientservice.dto.validators.CreatePatientValidationGroup;
import com.yoshatech.patientservice.service.PatientService;
import jakarta.validation.Valid;
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
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/get-patients")
    public ResponseEntity<List<PatientResponseDto>> getPatients() {
        log.info("Getting all the Patients");
        List<PatientResponseDto> patients = patientService.getPatients();

        return ResponseEntity.ok().body(patients );
    }

    @PostMapping("/create-patient")
    public ResponseEntity<PatientResponseDto> createPatient(@Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDto patientRequestDto) {
        log.info("Creating new patient with email: {}", patientRequestDto.getEmail());

        PatientResponseDto createdPatient = patientService.createPatient(patientRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDto> updatePatient(@PathVariable UUID id, @Validated({Default.class}) @RequestBody PatientRequestDto patientRequestDto) {
        log.info("Updating patient with id: {}", id);

        PatientResponseDto updatedPatient = patientService.updatePatient(id, patientRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(updatedPatient);
    }
}
