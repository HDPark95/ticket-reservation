package kr.hhplus.be.server.application.payment;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.ConcertService;
import kr.hhplus.be.server.domain.concert.Seat;
import kr.hhplus.be.server.domain.payment.PaymentCommand;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationService;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenService;
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
    private final WaitingTokenService waitingTokenService;

    @Transactional
    public PaymentResult pay(Long reservationId, Long userId) {
        Reservation reservation = reservationService.getReservation(reservationId, userId);
        Seat seat = concertService.getSeat(reservation.getSeatId());
        userService.usePoint(reservation.getUserId(), seat.getPrice());
        reservationService.complete(reservation);
        PaymentResult result = paymentService.make(new PaymentCommand(
                reservation.getUserId(),
                reservation.getId(),
                seat.getConcertSchedule().getDate(),
                seat.getSeatNumber(),
                seat.getPrice()
        ));
        waitingTokenService.expireToken(reservation.getUserId());
        return result;
    }

    @Transactional
    public Page<PaymentResult> getPaymentByUserId(Long userId, Pageable pageable) {
        return paymentService.getPaymentByUserId(userId, pageable);
    }
}
