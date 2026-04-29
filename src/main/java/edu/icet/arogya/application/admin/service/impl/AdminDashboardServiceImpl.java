package edu.icet.arogya.application.admin.service.impl;

import edu.icet.arogya.application.admin.dto.dashboard.*;
import edu.icet.arogya.application.admin.service.AdminDashboardService;
import edu.icet.arogya.modules.appointment.entity.enums.AppointmentStatus;
import edu.icet.arogya.modules.appointment.repository.AppointmentRepository;
import edu.icet.arogya.modules.doctor.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public DashboardOverviewResponse getOverview() {
        long totalAppointments = appointmentRepository.countTotalAppointments();

        LocalDate today = LocalDate.now();
        long todayAppointments = appointmentRepository.countAppointmentsByDate(today);

        long confirmed = appointmentRepository.countAppointmentsByStatus(
                AppointmentStatus.CONFIRMED
        );

        long completed = appointmentRepository.countAppointmentsByStatus(
                AppointmentStatus.COMPLETED

        );

        long cancelled = appointmentRepository.countAppointmentsByStatus(
                AppointmentStatus.CANCELLED
        );

        long noShow = appointmentRepository.countAppointmentsByStatus(
                AppointmentStatus.NO_SHOW
        );

        long activeDoctors = doctorRepository.countActiveDoctors();

        return DashboardOverviewResponse.builder()
                .totalAppointments(totalAppointments)
                .todayAppointments(todayAppointments)
                .confirmedAppointments(confirmed)
                .completedAppointments(completed)
                .cancelledAppointments(cancelled)
                .noShowAppointments(noShow)
                .activeDoctors(activeDoctors)
                .build();
    }

    @Override
    public List<AppointmentTrendResponse> getTrends(int days) {

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);

        List<Object[]> results = appointmentRepository
                .findDailyAppointmentTrendsWithStatuses(startDate, endDate);

        return results.stream()
                .map(row -> AppointmentTrendResponse.builder()
                            .date(row[0].toString())
                            .totalAppointments(((Number) row[1]).longValue())
                            .completedAppointments(((Number) row[2]).longValue())
                            .cancelledAppointments(((Number) row[3]).longValue())
                            .build())
                .toList();
    }

    @Override
    public List<DoctorWorkloadResponse> getDoctorWorkload(LocalDate date) {
        List<Object[]> results = appointmentRepository.findDoctorWorkload(date);
        return results.stream()
                .map(row -> DoctorWorkloadResponse.builder()
                            .doctorId((UUID) row[0])
                            .doctorName((String) row[1])
                            .totalAppointments(((Number) row[2]).longValue())
                            .completedAppointments(((Number) row[3]).longValue())
                            .build())
                .toList();
    }

    @Override
    public List<CancellationSummaryResponse> getCancellations(int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);

        List<Object[]> results = appointmentRepository.findDailyCancellations(startDate, endDate);

        Map<LocalDate, Long> map = results.stream()
                .collect(Collectors.toMap(
                        row -> (LocalDate) row[0],
                        row -> ((Number) row[1]).longValue()
                ));

        List<CancellationSummaryResponse> response = new ArrayList<>();

        for (int i=0; i<=days; i++) {
            LocalDate date = startDate.plusDays(i);

            response.add(
                    CancellationSummaryResponse.builder()
                            .date(date.toString())
                            .cancellations(map.getOrDefault(date, 0L))
                            .build()
            );
        }
        return response;
    }

    @Override
    public List<NoShowSummaryResponse> getNoShows(int days) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days);

        List<Object[]> results = appointmentRepository.findDailyNoShows(startDate, endDate);

        Map<LocalDate, Long> map = results.stream()
                .collect(Collectors.toMap(
                        row -> (LocalDate) row[0],
                        row -> ((Number) row[1]).longValue()
                ));

        List<NoShowSummaryResponse> response = new ArrayList<>();

        for (int i=0; i<=days; i++) {
            LocalDate date = startDate.plusDays(i);

            response.add(
                    NoShowSummaryResponse.builder()
                            .date(date.toString())
                            .noShows(map.getOrDefault(date, 0L))
                            .build()
            );
        }
        return response;
    }
}
