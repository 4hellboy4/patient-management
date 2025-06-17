package com.yoshatech.patientservice.kafka;

import com.yoshatech.patientservice.model.Patient;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Slf4j
@Service
public class KafkaProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public void sendMessage(Patient patient) {
        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED")
                .build();

        log.info("Sending message to patient: {}", event);

        try {
            kafkaTemplate.send("patients", event.toByteArray());
        } catch (Exception e) {
            log.error("Error sending the PATIENT_CREATED request:{}", e.getMessage());
        }
    }
}
