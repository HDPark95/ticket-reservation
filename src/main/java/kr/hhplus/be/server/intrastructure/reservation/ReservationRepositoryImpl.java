package kr.hhplus.be.server.intrastructure.reservation;

import kr.hhplus.be.server.domain.concert.Seat;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import kr.hhplus.be.server.domain.reservation.ReservationStatus;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJPARepository reservationJPARepository;
    private final ReservationQuerydslRepository reservationQuerydslRepository;

    @Override
    public List<Reservation> getValidReservationsByScheduleId(Long scheduleId, LocalDateTime now) {
        return reservationQuerydslRepository.getValidReservationsByScheduleId(scheduleId, now);
    }

    @Override
    public Reservation save(Reservation pending) {
        return reservationJPARepository.save(pending);
    }

    @Override
    public Optional<Reservation> findByIdForUpdate(Long reservationId) {
        return reservationQuerydslRepository.findByIdForUpdate(reservationId);
    }

    @Override
    public List<Reservation> findAll() {
        return reservationJPARepository.findAll();
    }

    @Override
    public void deleteAll() {
        reservationJPARepository.deleteAll();
    }

    @Override
    public Optional<Reservation> findAlreadySeatReservation(Seat seat) {
        return reservationQuerydslRepository.findAlreadySeatReservation(seat);
    }

}
