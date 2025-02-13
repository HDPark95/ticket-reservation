package kr.hhplus.be.server.intrastructure.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class PaymentRedisRepository {
    private final RedisTemplate<String, String> paymentRedisTemplate;

    private final String PAYMENT_FAIL_QUEUE = "payment_fail_queue";

    public void addFailedPayment(Long reservationId, Long now) {
        paymentRedisTemplate.opsForZSet().add(PAYMENT_FAIL_QUEUE, String.valueOf(reservationId), now);
    }

    public List<Long> getFailedPaymentReservationIds() {
        Set<ZSetOperations.TypedTuple<String>> events = paymentRedisTemplate.opsForZSet().popMin(PAYMENT_FAIL_QUEUE, 10);
        return events == null ?
                List.of() : events.stream()
                .map(t -> Long.parseLong(t.getValue())).toList();
    }
}
