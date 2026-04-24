package edu.icet.arogya.modules.doctor.service;

import edu.icet.arogya.modules.doctor.dto.*;

import java.util.UUID;

public interface DoctorService {
    DoctorDetailsResponse getMyProfile(UUID userId);
    DoctorDetailsResponse updateMyProfile(UUID userId, DoctorSelfUpdateRequest request);
}
