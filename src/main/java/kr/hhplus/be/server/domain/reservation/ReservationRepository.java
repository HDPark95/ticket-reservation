package kr.hhplus.be.server.domain.reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    public List<Reservation> getValidReservationsByScheduleId(Long scheduleId, LocalDateTime now);

    Reservation save(Reservation pending);

    Optional<Reservation> findByIdAndUserIdForUpdate(Long reservationId, Long userId);

    List<Reservation> findAll();

    void deleteAll();

    Optional<Reservation> findAlreadySeatReservation(Long seatId);

    Optional<Reservation> findById(Long id);

    void addFailCompleteReservation(Long reservationId, Long now);

    List<Long> getFailCompleteReservationIds();

}
