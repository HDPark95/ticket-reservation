package kr.hhplus.be.server.application.userpoint;

import kr.hhplus.be.server.application.reservation.ReservationCompleteFailedEvent;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationService;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPointListener {
    private final UserService userService;
    private final ReservationService reservationService;
    private final PaymentService paymentService;

    @EventListener
    public void handleReservationCompleteFailEvent(ReservationCompleteFailedEvent event) {
        try{
            Payment payment = paymentService.getPaymentByReservationId(event.reservationId());
            userService.addPoint(payment.getUser().getId(), payment.getSeatPrice());
        } catch (Exception e) {
            reservationService.addFailCompleteReservation(event.reservationId());
        }
    }
}
