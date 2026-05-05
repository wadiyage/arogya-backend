package edu.icet.arogya.application.admin.doctor.service;

import edu.icet.arogya.application.admin.doctor.dto.AdminUpdateDoctorRequest;
import edu.icet.arogya.application.admin.doctor.dto.CreateDoctorRequest;
import edu.icet.arogya.modules.doctor.dto.DoctorDetailsResponse;
import edu.icet.arogya.application.admin.doctor.dto.DoctorResponse;
import edu.icet.arogya.security.user.UserPrincipal;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface AdminDoctorService {
    DoctorResponse createDoctor(UserPrincipal user, CreateDoctorRequest request);
    DoctorDetailsResponse updateDoctor(UserPrincipal user, UUID id, AdminUpdateDoctorRequest request);

    void deactivateDoctor(UserPrincipal user, UUID id, boolean available);

    DoctorDetailsResponse getDoctorDetails(UUID id);
    Page<@NonNull DoctorResponse> getAllDoctors(Pageable pageable);
}
