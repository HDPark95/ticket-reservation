package kr.hhplus.be.server.infrastructor.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component
@RequiredArgsConstructor
public class TestKafkaConsumer {

    private CountDownLatch latch = new CountDownLatch(5);
    private List<Long> reservationIds = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "test-consume", groupId = "test-group")
    public void listen(List<byte[]> messages, Acknowledgment ack) throws IOException {

        for (byte[] message : messages) {
            Long reservationId = objectMapper.readValue(message, Long.class);
            reservationIds.add(reservationId);
            latch.countDown();
        }
        ack.acknowledge();
    }

    public List<Long> getReservationIds() {
        return reservationIds;
    }

    public CountDownLatch getLatch() {
        return latch;
    }
}
