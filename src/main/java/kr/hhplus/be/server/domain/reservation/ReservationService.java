package kr.hhplus.be.server.domain.reservation;

import kr.hhplus.be.server.domain.concert.Seat;
import kr.hhplus.be.server.domain.user.User;

import java.util.List;

public interface ReservationService {
    List<Reservation> getValidReservationsByScheduleId(Long scheduleId);

    Reservation reserve(User user, Seat seat) ;
}
