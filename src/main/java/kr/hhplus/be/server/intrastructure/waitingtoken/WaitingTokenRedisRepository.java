package kr.hhplus.be.server.intrastructure.waitingtoken;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class WaitingTokenRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private final String WAITING_QUEUE = "waiting_queue";
    private final String ACTIVE_QUEUE = "active_queue";
    private final String USER_TOKEN_MAPPING = "user_token_mapping";
    private final String TOKEN_USER_MAPPING = "token_user_mapping";

    public String addToken(Long userId, String token) {
        Object existingToken = redisTemplate.opsForHash().get(USER_TOKEN_MAPPING, userId.toString());
        if (existingToken != null) {
            return existingToken.toString();
        }
        redisTemplate.opsForHash().putIfAbsent(USER_TOKEN_MAPPING, userId.toString(), token);

        redisTemplate.opsForHash().putIfAbsent(TOKEN_USER_MAPPING, token, userId.toString());
        return token;
    }

    public void addToWaitingQueue(String token, long timestamp) {
        redisTemplate.opsForZSet().addIfAbsent(WAITING_QUEUE, token, timestamp);
    }

    public Long getQueuePosition(String token) {
        return redisTemplate.opsForZSet().rank(WAITING_QUEUE, token);
    }

    public Set<String> getUsersFromQueue(Long count) {
        Set<ZSetOperations.TypedTuple<String>> result = redisTemplate.opsForZSet().popMin(WAITING_QUEUE, count);

        if (result == null) return Set.of();

        return result.stream()
                .map(tuple -> String.valueOf(tuple.getValue()))
                .collect(Collectors.toSet());
    }

    public void addActivateQueue(Set<String> tokensToActivate, long expiryTimestamp) {
        tokensToActivate.forEach(token -> redisTemplate.opsForHash().put(ACTIVE_QUEUE, token, String.valueOf(expiryTimestamp)));
    }

    public int deleteExpiredTokens(long currentTimestamp) {
        Map<Object, Object> activeTokens = redisTemplate.opsForHash().entries(ACTIVE_QUEUE);

        Map<String, Long> tokenExpiryMap = activeTokens.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toString(),
                        entry -> Long.parseLong(entry.getValue().toString())
                ));

        Set<String> expiredTokens = tokenExpiryMap.entrySet().stream()
                .filter(entry -> entry.getValue() < currentTimestamp)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        if (expiredTokens.isEmpty()) {
            return 0;
        }

        for (String token : expiredTokens) {
            Object userId = redisTemplate.opsForHash().get(TOKEN_USER_MAPPING, token);

            if (userId != null) {
                redisTemplate.opsForHash().delete(TOKEN_USER_MAPPING, token);
                redisTemplate.opsForHash().delete(USER_TOKEN_MAPPING, userId.toString());
            }
            redisTemplate.opsForHash().delete(ACTIVE_QUEUE, token);
        }
        return expiredTokens.size();
    }

    public Long getActiveUserCount() {
        return redisTemplate.opsForHash().size(ACTIVE_QUEUE);
    }

    public Boolean isActiveToken(String token) {
        return redisTemplate.opsForHash().hasKey(ACTIVE_QUEUE, token);
    }

    public Optional<Object> findUserIdByToken(String token) {
        return Optional.ofNullable(redisTemplate.opsForHash().get(USER_TOKEN_MAPPING, token));
    }
}
