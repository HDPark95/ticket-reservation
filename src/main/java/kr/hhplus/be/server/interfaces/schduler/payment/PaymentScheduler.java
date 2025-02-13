package kr.hhplus.be.server.interfaces.schduler.payment;

import kr.hhplus.be.server.application.payment.PaymentFailedEvent;
import kr.hhplus.be.server.domain.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentScheduler {
    private final PaymentService paymentService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Scheduled(fixedDelay = 10000)
    public void failPaymentPublish() {
        List<Long> failedPaymentReservationIds = paymentService.getFailedPaymentReservationIds();
        failedPaymentReservationIds.forEach(reservationId -> applicationEventPublisher.publishEvent(new PaymentFailedEvent(reservationId)));
    }

}
