package kr.hhplus.be.server.interfaces.schduler.outbox;

import kr.hhplus.be.server.domain.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxRetryScheduler {

    private final PaymentService paymentService;

    @Scheduled(fixedDelay = 1000)
    public void resendingPaymentComplete() {
        paymentService.resendPendingMessage();
    }
}
