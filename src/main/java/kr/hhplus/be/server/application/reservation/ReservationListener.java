package kr.hhplus.be.server.application.reservation;

import kr.hhplus.be.server.application.payment.PaymentFailedEvent;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationListener {
    private final ReservationService reservationService;
    private final PaymentService paymentService;

    @EventListener
    public void handlePaymentFailedEvent(PaymentFailedEvent event) {
        try{
            Long reservationId = event.reservationId();

            reservationService.rollbackReservationComplete(reservationId);
        } catch (Exception e) {
            paymentService.addFailedPayment(event.reservationId());
        }

    }
}
