package edu.icet.arogya.modules.appointment.validation;

import edu.icet.arogya.common.exception.BadRequestException;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;

import java.util.Map;
import java.util.Set;

public class AppointmentStatusTransitionValidator {
    private static final Map<AppointmentStatus, Set<AppointmentStatus>> ALLOWED_TRANSITIONS = Map.of(
            AppointmentStatus.PENDING, Set.of(AppointmentStatus.CONFIRMED, AppointmentStatus.CANCELLED),
            AppointmentStatus.CONFIRMED, Set.of(AppointmentStatus.CHECKED_IN, AppointmentStatus.CANCELLED),
            AppointmentStatus.CHECKED_IN, Set.of(AppointmentStatus.IN_PROGRESS, AppointmentStatus.CANCELLED),
            AppointmentStatus.IN_PROGRESS, Set.of(AppointmentStatus.COMPLETED),
            AppointmentStatus.COMPLETED, Set.of(),
            AppointmentStatus.CANCELLED, Set.of(),
            AppointmentStatus.NO_SHOW, Set.of()
    );

    public static void validate(AppointmentStatus current, AppointmentStatus target) {
        Set<AppointmentStatus> allowed = ALLOWED_TRANSITIONS.getOrDefault(current, Set.of());
        if(!allowed.contains(target)) {
            throw new BadRequestException(
                    "Invalid status transition from " + current + " to " + target
            );
        }
    }
}
