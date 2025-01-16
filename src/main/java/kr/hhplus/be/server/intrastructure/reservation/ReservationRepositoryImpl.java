package kr.hhplus.be.server.intrastructure.reservation;

import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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
}
