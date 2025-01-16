package kr.hhplus.be.server.domain.reservation;

import kr.hhplus.be.server.domain.concert.Seat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    public List<Reservation> getValidReservationsByScheduleId(Long scheduleId, LocalDateTime now);

    Reservation save(Reservation pending);

    Optional<Reservation> findByIdForUpdate(Long reservationId);

    List<Reservation> findAll();

    void deleteAll();

    Optional<Reservation> findAlreadySeatReservation(Seat seat);
}
