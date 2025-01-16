package kr.hhplus.be.server.domain.concert;

import kr.hhplus.be.server.domain.user.User;

import java.util.List;

public interface ConcertService {

    List<ConcertSchedule> getSchedules(Long concertId);

    List<Seat> getAvailableSeats(Long scheduleId, List<Long> reservedSeatIds);

    Seat getSeat(Long seatId);

}
