package kr.hhplus.be.server.infrastructor.kafka;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@SpringBootTest
public class KafkaTest {

    @Autowired
    TestKafkaProducer producer;

    @Autowired
    TestKafkaConsumer consumer;

    @Test
    @DisplayName("test-consume 메시지를 5번 발송하고, 5번 수신한다.")
    public void sendMessageAndListenPayload() throws InterruptedException {
        for (Long i = 1L; i <= 5L; i++) {
            producer.send("test-consume", i);
        }

        await()
                .pollInterval(300, TimeUnit.MILLISECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> Assertions.assertThat(consumer.getReservationIds())
                        .hasSize(5)
                        .containsExactly(1L, 2L, 3L, 4L, 5L));
    }

}
