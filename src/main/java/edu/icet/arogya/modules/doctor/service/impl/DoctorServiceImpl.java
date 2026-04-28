package edu.icet.arogya.modules.doctor.service.impl;

import edu.icet.arogya.common.exception.ResourceNotFoundException;
import edu.icet.arogya.modules.doctor.dto.*;
import edu.icet.arogya.modules.doctor.entity.Doctor;
import edu.icet.arogya.modules.doctor.mapper.DoctorMapper;
import edu.icet.arogya.modules.doctor.repository.DoctorRepository;
import edu.icet.arogya.modules.doctor.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    private final DoctorMapper doctorMapper;

    @Override
    public DoctorDetailsResponse getMyProfile(UUID userId) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + userId));
        return doctorMapper.mapToDetailsResponse(doctor);
    }

    @Override
    public DoctorDetailsResponse updateMyProfile(UUID userId, DoctorSelfUpdateRequest request) {
        Doctor doctor = doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with ID: " + userId));

        if(request.getFullName() != null) {
            doctor.setFullName(request.getFullName());
        }

        if(request.getQualification() != null) {
            doctor.setQualification(request.getQualification());
        }

        if(request.getPhoneNumber() != null) {
            doctor.setPhoneNumber(request.getPhoneNumber());
        }

        if(request.getAvailable() != null) {
            doctor.setAvailable(request.getAvailable());
        }

        doctorRepository.save(doctor);
        return doctorMapper.mapToDetailsResponse(doctor);
    }
}
