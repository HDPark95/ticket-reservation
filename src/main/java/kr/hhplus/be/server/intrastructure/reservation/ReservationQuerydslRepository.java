package kr.hhplus.be.server.intrastructure.reservation;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.concert.QSeat;
import kr.hhplus.be.server.domain.concert.Seat;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static kr.hhplus.be.server.domain.concert.QConcertSchedule.concertSchedule;
import static kr.hhplus.be.server.domain.concert.QSeat.*;
import static kr.hhplus.be.server.domain.concert.QSeat.seat;
import static kr.hhplus.be.server.domain.reservation.QReservation.reservation;

@Repository
@RequiredArgsConstructor
public class ReservationQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Reservation> getValidReservationsByScheduleId(Long scheduleId, LocalDateTime now) {
        return jpaQueryFactory.selectFrom(reservation)
                .innerJoin(reservation.seat, seat)
                .innerJoin(seat.concertSchedule, concertSchedule)
                .where(
                        concertSchedule.id.eq(scheduleId),
                        reservation.status.eq(ReservationStatus.RESERVED) // 예약된 좌석
                        .or(reservation.status.eq(ReservationStatus.PENDING).and(reservation.expiredAt.gt(now))) // 임시 예약된 좌석
                )
                .fetch();
    }

    public Optional<Reservation> findByIdForUpdate(Long reservationId) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(reservation)
                        .innerJoin(reservation.seat, seat).fetchJoin()
                        .innerJoin(seat.concertSchedule, concertSchedule).fetchJoin()
                        .innerJoin(concertSchedule.concert).fetchJoin()
                        .where(reservation.id.eq(reservationId))
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .fetchOne()
        );
    }

    public Optional<Reservation> findAlreadySeatReservation(Seat seat) {
        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(reservation)
                        .innerJoin(reservation.seat, QSeat.seat).fetchJoin()
                        .where(reservation.seat.eq(seat),
                                reservation.status.eq(ReservationStatus.PENDING).and(reservation.expiredAt.gt(LocalDateTime.now())).or(reservation.status.eq(ReservationStatus.RESERVED)))
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .fetchOne()
        );
    }
}
