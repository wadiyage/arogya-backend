package edu.icet.arogya.modules.appointment.dto.filter;

import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentFilterRequest {
    private UUID doctorId;
    private UUID patientId;

    private AppointmentStatus status;

    private LocalDate fromDate;
    private LocalDate toDate;

    private Boolean includeCancelled;
    private Boolean includePast;
    private Boolean todayOnly;
}
