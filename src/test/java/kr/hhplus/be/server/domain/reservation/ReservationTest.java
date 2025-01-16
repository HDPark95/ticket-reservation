package kr.hhplus.be.server.domain.reservation;

import kr.hhplus.be.server.domain.concert.Seat;
import kr.hhplus.be.server.domain.user.User;
import org.junit.Assert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class ReservationTest {

    @Test
    @DisplayName("예약 대기 생성 - PEDING 상태, 만료시간 30분 이후")
    public void reserve() {
        // given
        User user = User.builder().build();
        Seat seat = Seat.builder().build();
        LocalDateTime fixedNow = LocalDateTime.of(2025, 1, 1, 12, 0, 0);

        // when
        Reservation reservation = Reservation.createPending(user, seat, fixedNow);

        // then
        // 예약 대기 상태인가?
        Assert.assertEquals(ReservationStatus.PENDING, reservation.getStatus());
        // 만료시간이 30분 이후인가
        Assert.assertEquals(fixedNow.plusMinutes(30), reservation.getExpiredAt());
    }

    @Test
    @DisplayName("예약 대기 상태에서 예약 완료 - RESERVED 상태")
    public void reserved() {
        // given
        User user = User.builder().build();
        Seat seat = Seat.builder().build();

        LocalDateTime createdTime = LocalDateTime.of(2025, 1, 1, 12, 0, 0);
        Reservation reservation = Reservation.createPending(user, seat, createdTime);
        //만료 시간 - 20분 이후
        LocalDateTime reserveTime = LocalDateTime.of(2025, 1, 1, 12, 20, 0);
        // when
        reservation.reserved(reserveTime);

        // then
        // 예약 완료 상태인가?
        Assert.assertEquals(ReservationStatus.RESERVED, reservation.getStatus());
    }

    @Test
    @DisplayName("이미 예약 완료된 상태에서 예약 완료 - AlreadyPaidReservationException")
    public void reserved_already_reserved() {
        // given
        User user = User.builder().build();
        Seat seat = Seat.builder().build();

        LocalDateTime createdTime = LocalDateTime.of(2025, 1, 1, 12, 0, 0);
        Reservation reservation = Reservation.createPending(user, seat, createdTime);
        //만료 시간 - 20분 이후
        LocalDateTime reserveTime = LocalDateTime.of(2025, 1, 1, 12, 20, 0);
        reservation.reserved(reserveTime);

        // when
        // then
        Assert.assertThrows(AlreadyPaidReservationException.class, () -> reservation.reserved(reserveTime));
    }

    @Test
    @DisplayName("만료된 예약 대기 상태에서 예약 완료 - ReservationExpiredException")
    public void reserved_expired() {
        // given
        User user = User.builder().build();
        Seat seat = Seat.builder().build();

        LocalDateTime createdTime = LocalDateTime.of(2025, 1, 1, 12, 0, 0);
        Reservation reservation = Reservation.createPending(user, seat, createdTime);
        //만료 시간 - 40분 이후
        LocalDateTime reserveTime = LocalDateTime.of(2025, 1, 1, 12, 40, 0);

        // when
        // then
        Assert.assertThrows(ReservationExpiredException.class, () -> reservation.reserved(reserveTime));
    }
}
