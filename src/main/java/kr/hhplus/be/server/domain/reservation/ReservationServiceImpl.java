package kr.hhplus.be.server.domain.reservation;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.Seat;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{

    private final ReservationRepository reservationRepository;
    private final Clock clock;

    @Override
    @Transactional
    public List<Reservation> getValidReservationsByScheduleId(Long scheduleId) {
        return reservationRepository.getValidReservationsByScheduleId(scheduleId, LocalDateTime.now(clock));
    }

    @Override
    @Transactional
    public Reservation reserve(User user, Seat seat) {
        return reservationRepository.save(Reservation.createPending(user, seat, LocalDateTime.now(clock)));
    }

    @Override
    @Transactional
    public Reservation getReservation(Long reservationId) {
        return reservationRepository.findByIdForUpdate(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("예약 정보가 없습니다."));
    }

    @Override
    @Transactional
    public void complete(Reservation reservation) {
        reservation.reserved(LocalDateTime.now(clock));
    }


}
