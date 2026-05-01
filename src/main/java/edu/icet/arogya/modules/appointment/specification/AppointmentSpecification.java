package edu.icet.arogya.modules.appointment.specification;

import edu.icet.arogya.modules.appointment.dto.filter.AppointmentFilterRequest;
import edu.icet.arogya.modules.appointment.entity.Appointment;
import jakarta.persistence.criteria.Predicate;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AppointmentSpecification {

    public static Specification<@NonNull Appointment> withFilters(AppointmentFilterRequest filter) {
        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if(filter == null) {
                return cb.conjunction();
            }

            if(filter.getDoctorId() != null) {
                predicates.add(
                        cb.equal(root.get("doctor").get("id"), filter.getDoctorId())
                );
            }

            if(filter.getPatientId() != null) {
                predicates.add(
                        cb.equal(root.get("patient").get("id"), filter.getPatientId())
                );
            }

            if(filter.getStatus() != null) {
                predicates.add(
                        cb.equal(root.get("status"), filter.getStatus())
                );
            }

            if(Boolean.TRUE.equals(filter.getTodayOnly())) {
                predicates.add(
                        cb.equal(root.get("appointmentDate"), LocalDate.now())
                );
            } else {
                if(filter.getFromDate() != null && filter.getToDate() != null) {
                    predicates.add(
                            cb.between(
                                    root.get("appointmentDate"),
                                    filter.getFromDate(),
                                    filter.getToDate()
                            )
                    );
                } else if(filter.getFromDate() != null) {
                    predicates.add(
                            cb.greaterThanOrEqualTo(
                                    root.get("appointmentDate"),
                                    filter.getFromDate()
                            )
                    );
                } else if(filter.getToDate() != null) {
                    predicates.add(
                            cb.lessThanOrEqualTo(
                                    root.get("appointmentDate"),
                                    filter.getToDate()
                            )
                    );
                }
            }

            if(Boolean.FALSE.equals(filter.getIncludeCancelled())) {
                predicates.add(
                        cb.notEqual(root.get("status"),
                                edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus.CANCELLED)
                );
            }

            if(Boolean.FALSE.equals(filter.getIncludePast())) {
                predicates.add(
                        cb.greaterThan(
                                root.get("appointmentDate"),
                                java.time.LocalDate.now()
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
