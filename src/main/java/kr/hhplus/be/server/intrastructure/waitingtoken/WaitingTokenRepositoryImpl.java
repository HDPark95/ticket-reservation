package kr.hhplus.be.server.intrastructure.waitingtoken;

import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class WaitingTokenRepositoryImpl implements WaitingTokenRepository {

    private final WaitingTokenRedisRepository redisRepository;

    @Override
    public void addToWaitingQueue(String token, long timestamp) {
        redisRepository.addToWaitingQueue(token, timestamp);
    }

    @Override
    public Long getPosition(String token) {
        return redisRepository.getQueuePosition(token);
    }

    @Override
    public Long getActiveTokenCount() {
        return redisRepository.getActiveUserCount();
    }

    @Override
    public int deleteExpiredTokens(long currentTimestamp) {
        return redisRepository.deleteExpiredTokens(currentTimestamp);
    }

    @Override
    public Set<String> getTokensFromQueueForActive(Long availableSlots) {
        return redisRepository.getUsersFromQueue(availableSlots);
    }

    @Override
    public void activateTokens(Set<String> usersToActivate, long expiryTimestamp) {
        redisRepository.addActivateQueue(usersToActivate, expiryTimestamp);
    }

    @Override
    public String addToken(Long id, String token) {
        return redisRepository.addToken(id, token);
    }

    @Override
    public Boolean isActiveToken(String token) {
        return redisRepository.isActiveToken(token);
    }

    @Override
    public Optional<Object> findUserIdByToken(String token) {
        return redisRepository.findUserIdByToken(token);
    }

    @Override
    public void expireToken(Long userId) {
        redisRepository.expireToken(userId);
    }
}
