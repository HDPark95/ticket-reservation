package kr.hhplus.be.server.domain.concert;

import java.util.List;
import java.util.Optional;

public interface ConcertRepository {
    List<Seat> getAvailableSeats(Long scheduleId, List<Long> reservedSeatIds);

    List<ConcertSchedule> getSchedules(Long concertId);

    Concert save(Concert concert);

    Optional<Seat> findSeatForUpdate(Long seatId);

    Seat saveSeat(Seat seat);
    ConcertSchedule saveSchedule(ConcertSchedule concertSchedule);

    Optional<Seat> findSeat(Long seatId);
}
