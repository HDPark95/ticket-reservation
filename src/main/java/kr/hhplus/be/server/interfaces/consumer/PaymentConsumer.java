package kr.hhplus.be.server.interfaces.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.payment.PaymentCompleteOutbox;
import kr.hhplus.be.server.domain.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentService paymentService;

    private ObjectMapper objectMapper = new ObjectMapper();
    @KafkaListener(topics = "payment-complete-event", groupId = "payment-complete-self")
    public void handlePaymentCompleteEvent(List<byte[]> eventIds, Acknowledgment ack) {
        List<Long> paymentIds = eventIds.stream()
                .map(bytes -> {
                    try {
                        return objectMapper.readValue(bytes, Long.class);
                    } catch (IOException e) {
                        return null;
                    }
                }).filter(id -> id != null).collect(Collectors.toList());

        paymentService.updatePaymentCompleteMessageStatus(paymentIds, PaymentCompleteOutbox.Status.SUCCESS);
        ack.acknowledge();
    }

}
