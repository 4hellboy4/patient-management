package com.yoshatech.patientservice.dto;


import com.yoshatech.patientservice.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PatientRequestDto {
    @NotBlank(message = "Name is Required")
    @Size(max = 50, message = "The name must not exceed 50 characters")
    private String name;

    @NotBlank(message = "Email is Required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Address is Required")
    private String address;

    @NotBlank(message = "Date of Birth is required")
    private String dateOfBirth;

    @NotBlank(groups = {CreatePatientValidationGroup.class}, message = "Registered Date is required")
    private String registeredDate;
}
