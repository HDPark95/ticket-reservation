package kr.hhplus.be.server.infrastructor.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TestKafkaProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, Long reservationId) {
        kafkaTemplate.send(topic, reservationId.toString());
    }
}
