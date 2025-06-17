package com.yoshatech.patientservice.service;

import com.yoshatech.patientservice.dto.PatientRequestDto;
import com.yoshatech.patientservice.dto.PatientResponseDto;
import com.yoshatech.patientservice.exception.EmailAlreadyExistsException;
import com.yoshatech.patientservice.exception.UserNotFoundException;
import com.yoshatech.patientservice.grpc.BillingServiceGrpcClient;
import com.yoshatech.patientservice.kafka.KafkaProducer;
import com.yoshatech.patientservice.mapper.PatientMapper;
import com.yoshatech.patientservice.model.Patient;
import com.yoshatech.patientservice.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    public List<PatientResponseDto> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream().map(PatientMapper::toDto).toList();
    }

    @Transactional
    public PatientResponseDto createPatient(PatientRequestDto patientRequest) {
        doesEmailExist(patientRequest.getEmail(), null);

        Patient newPatient = PatientMapper.toPatient(patientRequest);
        patientRepository.save(newPatient);

        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(), newPatient.getName(), newPatient.getEmail());

        kafkaProducer.sendMessage(newPatient);

        return PatientMapper.toDto(newPatient);
    }

    public PatientResponseDto updatePatient(UUID id, PatientRequestDto patientRequest) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("Patient with id " + id + " not found")
        );

        doesEmailExist(patientRequest.getEmail(), id);

        patient.setName(patientRequest.getName());
        patient.setAddress(patientRequest.getAddress());
        patient.setEmail(patientRequest.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequest.getDateOfBirth()));

        patientRepository.save(patient);

        return PatientMapper.toDto(patient);
    }

    public void doesEmailExist(String email, UUID patientId) {
        if (patientId == null) {
            if (patientRepository.existsByEmail(email)) {
                throw new EmailAlreadyExistsException("A patient with this email " + email + " already exists" );
            }
        } else {
            if (patientRepository.existsByEmailAndIdNot(email, patientId)) {
                throw new EmailAlreadyExistsException("A patient with this email " + email + " already exists" );
            }
        }
    }

    public void deletePatient(UUID id) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("Patient with id " + id + " not found")
        );

        patientRepository.delete(patient);
    }
}
