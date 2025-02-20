package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.domain.payment.PaymentCompleteEvent;
import kr.hhplus.be.server.domain.payment.PaymentMessageProducer;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.infrastructure.payment.PaymentMessageProducerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class PaymentListener {

    private final PaymentService paymentService;
    private final PaymentMessageProducer paymentMessageProducer;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void putPaymentCompleteMessageToOutbox(PaymentCompleteEvent event) {
        paymentService.putPaymentCompleteMessageToOutbox(event.paymentId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleDataPortalEvent(PaymentCompleteEvent paymentCompleteEvent) {
        paymentMessageProducer.publishPaymentComplete(paymentCompleteEvent.paymentId());
    }

}
