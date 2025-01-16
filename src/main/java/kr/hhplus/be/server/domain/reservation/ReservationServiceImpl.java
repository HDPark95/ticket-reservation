package kr.hhplus.be.server.domain.reservation;

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
    public List<Reservation> getValidReservationsByScheduleId(Long scheduleId) {
        return reservationRepository.getValidReservationsByScheduleId(scheduleId, LocalDateTime.now(clock));
    }

    @Override
    public Reservation reserve(User user, Seat seat) {
        return reservationRepository.save(Reservation.createPending(user, seat, LocalDateTime.now(clock)));
    }
}
