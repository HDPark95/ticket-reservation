package kr.hhplus.be.server.intrastructure.concert;

import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.concert.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJPARepository concertJPARepository;
    private final ConcertSchduleJPARepository concertScheduleJPARepository;
    private final SeatJPARepository seatJPARepository;
    private final ConcertQuerydslRepository concertQuerydslRepository;

    @Override
    public List<Seat> getAvailableSeats(Long scheduleId, List<Long> reservedSeatIds) {
        return concertQuerydslRepository.getAvailableSeats(scheduleId, reservedSeatIds);
    }

    @Override
    public List<ConcertSchedule> getSchedules(Long concertId) {
        return concertQuerydslRepository.getSchedules(concertId);
    }

    @Override
    public Concert save(Concert concert) {
        return concertJPARepository.save(concert);
    }

    @Override
    public Optional<Seat> findSeatForUpdate(Long seatId) {
        return concertQuerydslRepository.findByIdForUpdate(seatId);
    }

    @Override
    public Seat saveSeat(Seat seat) {
        return seatJPARepository.save(seat);
    }

    @Override
    public ConcertSchedule saveSchdule(ConcertSchedule concertSchedule) {
        return concertScheduleJPARepository.save(concertSchedule);
    }
}
