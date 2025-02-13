package kr.hhplus.be.server.application.payment;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.reservation.*;
import kr.hhplus.be.server.domain.user.InsufficientPointsException;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.*;

import static org.mockito.Mockito.when;

@SpringBootTest
public class PaymentFacadeTest {

    @Autowired
    private PaymentFacade paymentFacade;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConcertRepository concertRepository;

    @MockitoBean
    private Clock clock;

    @Autowired
    EntityManager em;

    private void setMockClock() {
        Instant fixedInstant = Instant.parse("2025-01-01T10:00:00Z");
        ZoneId zoneId = ZoneId.of("UTC");

        when(clock.instant()).thenReturn(fixedInstant);
        when(clock.getZone()).thenReturn(zoneId);
    }

    private User createTestUser(String name, BigDecimal points, String phoneNumber) {
        return userRepository.save(
                User.builder()
                        .name(name)
                        .point(points)
                        .phoneNumber(phoneNumber)
                        .build()
        );
    }

    private Concert createTestConcert(String name) {
        return concertRepository.save(
                Concert.builder()
                        .name(name)
                        .build()
        );
    }

    private ConcertSchedule createTestSchedule(Concert concert, int seatCount, BigDecimal price) {
        ConcertSchedule schedule = ConcertSchedule.builder()
                .concert(concert)
                .date(LocalDate.now(clock))
                .build();

        for (int i = 0; i < seatCount; i++) {
            schedule.addSeat(i, price);
        }

        return concertRepository.saveSchdule(schedule);
    }

    @Test
    @Transactional
    @DisplayName("결제 완료 처리 - 성공")
    public void completePayment() {
        // given
        setMockClock();

        User user = createTestUser("박현두", BigDecimal.valueOf(10000L), "01012312411234");

        Concert concert = createTestConcert("아이유 콘서트");

        ConcertSchedule schedule = createTestSchedule(concert, 50, BigDecimal.valueOf(10000L));

        Reservation reservation = reservationRepository.save(
                Reservation.builder()
                        .userId(user.getId())
                        .seatId(schedule.getSeats().get(0).getId())
                        .status(ReservationStatus.PENDING) // 예약 상태 PENDING
                        .expiredAt(LocalDateTime.now(clock).plusMinutes(30)) // 30분 후 만료
                        .build()
        );

        // when && then
        Assertions.assertDoesNotThrow(() -> {
            paymentFacade.pay(reservation.getId(), user.getId());
        });
    }

    @Test
    @Transactional
    @DisplayName("결제 완료 처리시 존재하지 않는 예약 ID인 경우 ReservationNotFoundException 발생")
    public void completePaymentWithNonExistReservation() {
        User user = createTestUser("박현두", BigDecimal.valueOf(10000L), "0101234111234");
        // given
        Long reservationId = 999L;

        // when && then
        Assertions.assertThrows(ReservationNotFoundException.class, () -> {
            paymentFacade.pay(reservationId, user.getId());
        });
    }

    @Test
    @Transactional
    @DisplayName("결제 완료 처리시 이미 결제된 예약 ID인 경우 AlreadyPaidReservationException 발생")
    public void completePaymentWithAlreadyPaidReservation() {
        // given
        setMockClock();
        User user = createTestUser("박현두", BigDecimal.valueOf(10000L), "010121341231");
        Concert concert = createTestConcert("아이유 콘서트");
        ConcertSchedule schedule = createTestSchedule(concert, 50, BigDecimal.valueOf(10000L));

        Reservation reservation = reservationRepository.save(
                Reservation.builder()
                        .userId(user.getId())
                        .seatId(schedule.getSeats().get(0).getId())
                        .status(ReservationStatus.RESERVED) //이미 결제된 상태
                        .expiredAt(LocalDateTime.now(clock).plusMinutes(30)) // 30분 후 만료
                        .build()
        );

        // when && then
        Assertions.assertThrows(AlreadyPaidReservationException.class, () -> {
            paymentFacade.pay(reservation.getId(), user.getId());
        });
    }

    @Test
    @Transactional
    @DisplayName("결제 완료 처리시 expired_at을 지난 경우 ReservationExpiredException 발생")
    public void completePaymentWithExpiredReservation() {
        // given
        setMockClock();
        User user = createTestUser("박현두", BigDecimal.valueOf(10000L), "010123411232");

        Concert concert = createTestConcert("아이유 콘서트");
        ConcertSchedule schedule = createTestSchedule(concert, 50, BigDecimal.valueOf(10000L));

        Reservation reservation = reservationRepository.save(
                Reservation.builder()
                        .userId(user.getId())
                        .seatId(schedule.getSeats().get(0).getId())
                        .status(ReservationStatus.PENDING) // 결제 대기 상태
                        .expiredAt(LocalDateTime.now(clock).minusMinutes(30)) // 30분 전 만료
                        .build()
        );

        // when && then
        Assertions.assertThrows(ReservationExpiredException.class, () -> {
            paymentFacade.pay(reservation.getId(), user.getId());
        });
    }

    @Test
    @DisplayName("결제 완료 처리시 사용자 포인트가 부족한 경우 InsufficientPointException 발생")
    public void completePaymentWithInsufficientPoint() {
        // given
        setMockClock();
        User user = createTestUser("박현두", BigDecimal.valueOf(1000L), "01012341233");
        Concert concert = createTestConcert("아이유 콘서트");
        ConcertSchedule schedule = createTestSchedule(concert, 50, BigDecimal.valueOf(10000L));

        Reservation reservation = reservationRepository.save(
                Reservation.builder()
                        .userId(user.getId())
                        .seatId(schedule.getSeats().get(0).getId())
                        .status(ReservationStatus.PENDING) //이미 결제된 상태
                        .expiredAt(LocalDateTime.now(clock).plusMinutes(30)) // 30분 후 만료
                        .build()
        );

        // when && then
        Assertions.assertThrows(InsufficientPointsException.class, () -> {
            paymentFacade.pay(reservation.getId(), user.getId());
        });
    }
}
