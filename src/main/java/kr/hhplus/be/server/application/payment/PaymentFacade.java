package kr.hhplus.be.server.application.payment;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.ConcertService;
import kr.hhplus.be.server.domain.concert.Seat;
import kr.hhplus.be.server.domain.payment.PaymentCommand;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationService;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentFacade {

    private final ReservationService reservationService;
    private final PaymentService paymentService;
    private final UserService userService;
    private final ConcertService concertService;
    @Transactional
    public PaymentResult pay(Long reservationId) {
        // 예약 정보 조회
        Reservation reservation = reservationService.getReservation(reservationId);
        // 좌석 정보 조회
        Seat seat = concertService.getSeat(reservation.getSeatId());
        //사용자 포인트 사용
        userService.usePoint(reservation.getUserId(), seat.getPrice());
        // 예약 완료
        reservationService.complete(reservation);
        //결제 내역 생성
        return paymentService.make(new PaymentCommand(
                reservation.getUserId(),
                reservation.getId(),
                seat.getConcertSchedule().getDate(),
                seat.getSeatNumber(),
                seat.getPrice()
        ));
    }

    @Transactional
    public Page<PaymentResult> getPaymentByUserId(Long userId, Pageable pageable) {
        return paymentService.getPaymentByUserId(userId, pageable);
    }
}
