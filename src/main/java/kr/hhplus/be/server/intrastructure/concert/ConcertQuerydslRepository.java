package kr.hhplus.be.server.intrastructure.concert;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.concert.QConcertSchedule;
import kr.hhplus.be.server.domain.concert.QSeat;
import kr.hhplus.be.server.domain.concert.Seat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static kr.hhplus.be.server.domain.concert.QConcertSchedule.concertSchedule;
import static kr.hhplus.be.server.domain.concert.QSeat.seat;

@Repository
@RequiredArgsConstructor
public class ConcertQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Seat> getAvailableSeats(Long scheduleId, List<Long> reservedSeatIds) {
        return jpaQueryFactory.selectFrom(seat)
                .where(seat.concertSchedule.id.eq(scheduleId)
                        .and(seat.id.notIn(reservedSeatIds)))
                .fetch();
    }

    public List<ConcertSchedule> getSchedules(Long concertId) {
        return jpaQueryFactory.selectFrom(concertSchedule)
                .where(concertSchedule.concert.id.eq(concertId))
                .fetch();
    }


    public Optional<Seat> findByIdForUpdate(Long seatId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(seat)
                        .where(seat.id.eq(seatId))
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .fetchOne()
        );
    }
}
