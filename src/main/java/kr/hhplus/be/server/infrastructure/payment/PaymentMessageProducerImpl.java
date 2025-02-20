package kr.hhplus.be.server.infrastructure.payment;

import kr.hhplus.be.server.domain.payment.PaymentCompleteEvent;
import kr.hhplus.be.server.domain.payment.PaymentMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMessageProducerImpl implements PaymentMessageProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishPaymentComplete(Long paymentId) {
        kafkaTemplate.send("payment-complete", paymentId);
    }

}
