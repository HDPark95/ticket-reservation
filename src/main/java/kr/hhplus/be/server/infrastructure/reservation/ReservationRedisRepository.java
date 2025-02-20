package kr.hhplus.be.server.infrastructure.reservation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ReservationRedisRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final static String FAIL_COMPLETE_RESERVATION = "fail_complete_reservation";

    public void addFailCompleteReservation(Long reservationId, Long now) {
        redisTemplate.opsForZSet().add(FAIL_COMPLETE_RESERVATION, String.valueOf(reservationId), now);
    }

    public List<Long> getFailCompleteReservationIds() {
        Set<ZSetOperations.TypedTuple<String>> events = redisTemplate.opsForZSet().popMin(FAIL_COMPLETE_RESERVATION, 10);
        return events == null ?
                List.of() : events.stream()
                .map(t -> Long.parseLong(t.getValue())).toList();
    }
}
