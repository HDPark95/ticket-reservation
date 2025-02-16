package kr.hhplus.be.server.domain.reservation;

import java.util.List;

public interface ReservationService {
    List<Reservation> getValidReservationsByScheduleId(Long scheduleId);

    Reservation reserve(Long userId, Long seatId) ;

    Reservation getReservation(Long reservationId, Long userId);

    void complete(Reservation reservation);

    void rollbackReservationComplete(Long reservationId);

    List<Long> getFailCompleteReservationIds();

    void addFailCompleteReservation(Long reservationId);
}
