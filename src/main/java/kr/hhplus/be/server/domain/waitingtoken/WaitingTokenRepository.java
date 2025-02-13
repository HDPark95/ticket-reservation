package kr.hhplus.be.server.domain.waitingtoken;

import java.util.Optional;
import java.util.Set;

public interface WaitingTokenRepository {

    void addToWaitingQueue(String token, long timestamp);

    Long getPosition(String token);

    int deleteExpiredTokens(long currentTimestamp);

    Long getActiveTokenCount();

    Set<String> getTokensFromQueueForActive(Long availableSlots);

    void activateTokens(Set<String> usersToActivate, long expiryTimestamp);

    Boolean isActiveToken(String token);

    Optional<Object> findUserIdByToken(String token);

    String addToken(Long id, String token);

    void expireToken(Long userId);
}
