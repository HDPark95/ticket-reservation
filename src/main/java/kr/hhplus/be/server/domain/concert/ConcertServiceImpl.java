package kr.hhplus.be.server.domain.concert;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConcertServiceImpl implements ConcertService{

    private final ConcertRepository concertRepository;

    @Override
    @Transactional
    public List<ConcertSchedule> getSchedules(Long concertId) {
        return concertRepository.getSchedules(concertId);
    }

    @Override
    @Transactional
    public List<Seat> getAvailableSeats(Long scheduleId, List<Long> reservedSeatIds) {
        return concertRepository.getAvailableSeats(scheduleId, reservedSeatIds);
    }

    @Override
    public Seat getSeat(Long seatId) {
        return concertRepository.findSeat(seatId)
                .orElseThrow(() -> new SeatNotFoundException("존재하지 않는 좌석입니다."));
    }
}
