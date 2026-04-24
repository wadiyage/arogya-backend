package edu.icet.arogya.modules.doctor.mapper;

import edu.icet.arogya.modules.doctor.dto.DoctorDetailsResponse;
import edu.icet.arogya.application.admin.dto.doctor.DoctorResponse;
import edu.icet.arogya.modules.doctor.entity.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {
    public DoctorResponse mapToResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .userId(doctor.getUser().getId())
                .email(doctor.getUser().getEmail())
                .fullName(doctor.getFullName())
                .specialization(doctor.getSpecialization())
                .phoneNumber(doctor.getPhoneNumber())
                .hospitalName(doctor.getHospitalName())
                .consultationFee(doctor.getConsultationFee())
                .available(doctor.isAvailable())
                .createdAt(doctor.getCreatedAt())
                .build();
    }

    public DoctorDetailsResponse mapToDetailsResponse(Doctor doctor) {
        return DoctorDetailsResponse.builder()
                .id(doctor.getId())
                .userId(doctor.getUser().getId())
                .email(doctor.getUser().getEmail())
                .fullName(doctor.getFullName())
                .specialization(doctor.getSpecialization())
                .licenseNumber(doctor.getLicenseNumber())
                .qualification(doctor.getQualification())
                .phoneNumber(doctor.getPhoneNumber())
                .hospitalName(doctor.getHospitalName())
                .consultationFee(doctor.getConsultationFee())
                .available(doctor.isAvailable())
                .createdAt(doctor.getCreatedAt())
                .build();
    }
}
