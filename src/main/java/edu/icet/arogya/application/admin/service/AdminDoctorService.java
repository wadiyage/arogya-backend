package edu.icet.arogya.application.admin.service;

import edu.icet.arogya.application.admin.dto.doctor.AdminUpdateDoctorRequest;
import edu.icet.arogya.application.admin.dto.doctor.CreateDoctorRequest;
import edu.icet.arogya.modules.doctor.dto.DoctorDetailsResponse;
import edu.icet.arogya.application.admin.dto.doctor.DoctorResponse;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AdminDoctorService {
    DoctorResponse createDoctor(CreateDoctorRequest request);
    DoctorDetailsResponse updateDoctor(UUID id, AdminUpdateDoctorRequest request);

    void deactivateDoctor(UUID id, boolean available);

    DoctorDetailsResponse getDoctorDetails(UUID id);
    Page<@NonNull DoctorResponse> getAllDoctors(Pageable pageable);
}
