package edu.icet.arogya.modules.medicalrecord.repository;

import edu.icet.arogya.modules.medicalrecord.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, UUID> { }
