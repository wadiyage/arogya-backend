package edu.icet.arogya.modules.appointment.schedule.mapper;

import edu.icet.arogya.application.admin.schedule.dto.ScheduleResponse;
import edu.icet.arogya.modules.appointment.schedule.entity.DoctorSchedule;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {
    public ScheduleResponse mapToResponse(DoctorSchedule schedule) {
        return ScheduleResponse.builder()
                .id(schedule.getId())
                .doctorId(schedule.getDoctor().getId())
                .doctorName(schedule.getDoctor().getFullName())
                .locationId(schedule.getLocation().getId())
                .locationName(schedule.getLocation().getName())
                .date(schedule.getScheduleDate())
                .maxTokens(schedule.getMaxTokens())
                .bookedTokens(schedule.getBookedTokens())
                .active(schedule.getActive())
                .sessionName(schedule.getSessionName())
                .consultationFee(schedule.getConsultationFee())
                .build();
    }
}
