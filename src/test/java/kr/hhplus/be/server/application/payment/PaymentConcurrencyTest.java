package kr.hhplus.be.server.application.payment;

import jakarta.persistence.EntityManager;
import kr.hhplus.be.server.application.concert.ConcertResult;
import kr.hhplus.be.server.domain.concert.Concert;
import kr.hhplus.be.server.domain.concert.ConcertRepository;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.concert.Seat;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationRepository;
import kr.hhplus.be.server.domain.reservation.ReservationStatus;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PaymentConcurrencyTest {

    @Autowired
    private PaymentFacade paymentFacade;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("결제가 동시에 발생할 때 중복 결제가 발생하지 않는다.")
    void payConcurrently() throws InterruptedException {
        // given
        User user = userRepository.save(User.builder().name("박현두").phoneNumber("01012341236").point(BigDecimal.valueOf(10000)).build());
        Concert concert = concertRepository.save(Concert.builder().name("아이유 콘서트").build());
        ConcertSchedule schedule = concertRepository.saveSchdule(ConcertSchedule.builder().concert(concert).date(LocalDate.now()).build());
        Seat seat = concertRepository.saveSeat(Seat.builder().concertSchedule(schedule).seatNumber(1).price(BigDecimal.valueOf(10000L)).build());
        Reservation reservation = reservationRepository.save(
                Reservation.builder()
                        .user(user)
                        .seat(seat)
                        .status(ReservationStatus.PENDING)
                        .expiredAt(LocalDateTime.now().plusMinutes(30))
                        .build()
        );

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Callable<PaymentResult>> tasks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            tasks.add(() -> paymentFacade.pay(reservation.getId()));
        }
        // when
        executorService.invokeAll(tasks);

        // then
        Long count = paymentRepository.countByUserId(user.getId());
        assertEquals(1, count);
    }
}
