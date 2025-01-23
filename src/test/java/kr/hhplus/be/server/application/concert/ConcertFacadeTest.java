package kr.hhplus.be.server.application.concert;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.concert.Seat;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import kr.hhplus.be.server.domain.reservation.ReservationStatus;
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
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
public class ConcertFacadeTest {

    @Autowired
    private ConcertFacade concertFacade;
    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationRepository reservationRepository;

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

    @Test
    @DisplayName("콘서트 예약 성공 - 결제 대기 ")
    @Transactional
    void reserve() {
        // given
        setMockClock();

        User user = userRepository.save(User.builder().name("박현두").phoneNumber("01012341234").build());
        Concert concert = concertRepository.save(Concert.builder().name("아이유 콘서트").build());
        ConcertSchedule schedule = concertRepository.saveSchdule(ConcertSchedule.builder().concert(concert).date(LocalDate.now()).build());
        Seat seat = concertRepository.saveSeat(Seat.builder().concertSchedule(schedule).seatNumber(1).price(BigDecimal.valueOf(10000L)).build());

        ConcertCriteria.ReserveSeat command = new ConcertCriteria.ReserveSeat(seat.getId(), user.getId());
        // when
        ConcertResult.ReservationResult reserve = concertFacade.reserve(command);

        // then
        Assertions.assertEquals(ReservationStatus.PENDING, reserve.status());
        Assertions.assertEquals(0, seat.getPrice().compareTo(reserve.price()));
    }

    @Test
    @DisplayName("만료되지 않은 PENDING 상태의 예약이 존재하는 경우 49개의 좌석이 조회된다.")
    @Transactional
    void testGetAvailableSeatsWithPendingReservations() {
        // given
        setMockClock();

        User user = userRepository.save(User.builder().name("박현두").phoneNumber("01012341234").build());
        Concert concert = concertRepository.save(Concert.builder().name("아이유 콘서트").build());
        ConcertSchedule schedule = ConcertSchedule.builder().concert(concert).date(LocalDate.now(clock)).build();

        for (int i = 0; i <50L; i++) {
            schedule.addSeat(i, BigDecimal.valueOf(10000L));
        }

        schedule = concertRepository.saveSchdule(schedule);
        LocalDateTime fixedNow = LocalDateTime.now(clock);

        em.persist(Reservation.builder()
                .userId(user.getId())
                .seatId(schedule.getSeats().get(0).getId())
                .status(ReservationStatus.PENDING)
                .expiredAt(fixedNow.plusMinutes(10)) // 만료되지 않은 예약
                .build());

        // when
        List<ConcertResult.SeatInfo> availableSeats = concertFacade.getAvailableSeats(schedule.getId());

        // then
        Assertions.assertEquals(49, availableSeats.size());
    }

    @Test
    @DisplayName("만료된 PENDING 상태의 예약이 존재하는 경우 50개의 좌석이 조회된다.")
    @Transactional
    void getAvailableSeats() {
        // given
        setMockClock();

        User user = userRepository.save(User.builder().name("박현두").phoneNumber("01012341234").build());
        Concert concert = concertRepository.save(Concert.builder().name("아이유 콘서트").build());
        ConcertSchedule schedule = ConcertSchedule.builder().concert(concert).date(LocalDate.now(clock)).build();

        for (int i = 0; i <50L; i++) {
            schedule.addSeat(i, BigDecimal.valueOf(10000L));
        }

        schedule = concertRepository.saveSchdule(schedule);
        LocalDateTime fixedNow = LocalDateTime.now(clock);

        em.persist(Reservation.builder()
                .userId(user.getId())
                .seatId(schedule.getSeats().get(0).getId())
                .status(ReservationStatus.PENDING)
                .expiredAt(fixedNow.minusMinutes(10)) // 만료된 예약
                .build());

        // when
        List<ConcertResult.SeatInfo> availableSeats = concertFacade.getAvailableSeats(schedule.getId());

        // then
        Assertions.assertEquals(50, availableSeats.size());
    }

    @Test
    @DisplayName("RESERVED된 예약이 존재하는 경우 49개의 좌석이 조회된다.")
    @Transactional
    void getAvailableSeatsWithReservedReservations() {
        // given
        setMockClock();

        User user = userRepository.save(User.builder().name("박현두").phoneNumber("01012341234").build());
        Concert concert = concertRepository.save(Concert.builder().name("아이유 콘서트").build());
        ConcertSchedule schedule = ConcertSchedule.builder().concert(concert).date(LocalDate.now(clock)).build();
        for (int i = 0; i <50L; i++) {
            schedule.addSeat(i, BigDecimal.valueOf(10000L));
        }
        schedule = concertRepository.saveSchdule(schedule);
        LocalDateTime fixedNow = LocalDateTime.now(clock);

        em.persist(Reservation.builder()
                .userId(user.getId())
                .seatId(schedule.getSeats().get(0).getId())
                .status(ReservationStatus.RESERVED)
                .expiredAt(fixedNow.minusMinutes(10)) // RESERVED 상태의 예약
                .build());

        // when
        List<ConcertResult.SeatInfo> availableSeats = concertFacade.getAvailableSeats(schedule.getId());

        // then
        Assertions.assertEquals(49, availableSeats.size());
    }
}
