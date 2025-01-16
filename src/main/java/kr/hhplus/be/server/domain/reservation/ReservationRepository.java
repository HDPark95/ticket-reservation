package kr.hhplus.be.server.domain.reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository {

    public List<Reservation> getValidReservationsByScheduleId(Long scheduleId, LocalDateTime now);

    Reservation save(Reservation pending);
}
