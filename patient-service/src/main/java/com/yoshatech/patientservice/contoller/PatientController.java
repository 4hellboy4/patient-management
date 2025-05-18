package com.yoshatech.patientservice.contoller;

import com.yoshatech.patientservice.dto.PatientRequestDto;
import com.yoshatech.patientservice.dto.PatientResponseDto;
import com.yoshatech.patientservice.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<PatientResponseDto> createPatient(@Valid @RequestBody PatientRequestDto patientRequestDto) {
        PatientResponseDto createdPatient = patientService.createPatient(patientRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
    }
}
