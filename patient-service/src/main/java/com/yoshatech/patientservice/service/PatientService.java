package com.yoshatech.patientservice.service;

import com.yoshatech.patientservice.dto.PatientRequestDto;
import com.yoshatech.patientservice.dto.PatientResponseDto;
import com.yoshatech.patientservice.mapper.PatientMapper;
import com.yoshatech.patientservice.model.Patient;
import com.yoshatech.patientservice.repository.PatientRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public List<PatientResponseDto> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream().map(PatientMapper::toDto).toList();
    }

    public PatientResponseDto createPatient(PatientRequestDto patientRequest) {
        if (patientRepository.existsByEmail(patientRequest.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email " + patientRequest.getEmail() + " already exists" );
        }

        Patient newPatient = PatientMapper.toPatient(patientRequest);
        patientRepository.save(newPatient);

        return PatientMapper.toDto(newPatient);
    }
}
