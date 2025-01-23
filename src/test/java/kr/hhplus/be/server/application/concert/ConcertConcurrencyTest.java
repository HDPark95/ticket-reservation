package kr.hhplus.be.server.application.concert;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.concert.Seat;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.waitingtoken.WaitingToken;
import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ConcertConcurrencyTest {

    @Autowired
    ConcertFacade concertFacade;

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConcertRepository concertRepository;


    @Test
    @DisplayName("동시에 하나의 좌석에 여러 사용자가 예약 대기를 시도할때, 하나의 사용자만 성공해야 한다.")
    void reserveConcertSeat() throws InterruptedException {
        // given
        List<User> users = List.of(
                userRepository.save(User.builder().name("박").phoneNumber("01012341234").build()),
                userRepository.save(User.builder().name("박현").phoneNumber("01012341235").build()),
                userRepository.save(User.builder().name("박현두").phoneNumber("01012341236").build())
        );
        Concert concert = concertRepository.save(Concert.builder().name("아이유 콘서트").build());
        ConcertSchedule schedule = concertRepository.saveSchdule(ConcertSchedule.builder().concert(concert).date(LocalDate.now()).build());
        Seat seat = concertRepository.saveSeat(Seat.builder().concertSchedule(schedule).seatNumber(1).price(BigDecimal.valueOf(10000L)).build());
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Callable<ConcertResult.ReservationResult>> tasks = new ArrayList<>();
        users.forEach(user -> {
            ConcertCriteria.ReserveSeat command = new ConcertCriteria.ReserveSeat(seat.getId(), user.getId());
            tasks.add(() -> concertFacade.reserve(command));
        });

        // when
        executorService.invokeAll(tasks);

        // then
        List<Reservation> all = reservationRepository.findAll();
        assertEquals(1, all.size());
    }

}
