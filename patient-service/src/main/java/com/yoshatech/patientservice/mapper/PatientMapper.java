package com.yoshatech.patientservice.mapper;

import com.yoshatech.patientservice.dto.PatientRequestDto;
import com.yoshatech.patientservice.dto.PatientResponseDto;
import com.yoshatech.patientservice.model.Patient;

import java.time.LocalDate;

public class PatientMapper {

    public static PatientResponseDto toDto(Patient patient) {
        return PatientResponseDto.builder()
                .id(patient.getId().toString())
                .name(patient.getName())
                .email(patient.getEmail())
                .address(patient.getAddress())
                .dateOfBirth(patient.getDateOfBirth().toString())
                .build();
    }

    public static Patient toPatient(PatientRequestDto request) {
        return Patient.builder()
                .name(request.getName())
                .email(request.getEmail())
                .address(request.getAddress())
                .dateOfBirth(LocalDate.parse(request.getDateOfBirth()))
                .registeredDate(LocalDate.parse(request.getRegisteredDate()))
                .build();
    }
}
