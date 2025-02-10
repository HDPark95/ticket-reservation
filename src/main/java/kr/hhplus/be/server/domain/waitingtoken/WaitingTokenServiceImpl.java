package kr.hhplus.be.server.domain.waitingtoken;

import kr.hhplus.be.server.domain.user.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WaitingTokenServiceImpl implements WaitingTokenService {

    private final WaitingTokenRepository waitingTokenRepository;
    private final Clock clock;
    private final int MAX_ACTIVE_TOKENS = 100;

    @Override
    public WaitingTokenResult issue(Long id) {
        String token = UUID.randomUUID().toString();
        token =  waitingTokenRepository.addToken(id, token);

        waitingTokenRepository.addToWaitingQueue(token, Instant.now(clock).toEpochMilli());
        Long position = waitingTokenRepository.getPosition(token);
        return WaitingTokenResult.from(token, position);
    }

    @Override
    public WaitingTokenResult getWaitingTokenInfo(String token) {
        Boolean isActiveToken = waitingTokenRepository.isActiveToken(token);
        if (isActiveToken) {
            return WaitingTokenResult.from(token, 0L);
        }
        Long position = waitingTokenRepository.getPosition(token);
        return WaitingTokenResult.from(token, position);
    }

    @Override
    public void refreshWaitingTokens() {
        long currentTimestamp = Instant.now(clock).toEpochMilli();
        waitingTokenRepository.deleteExpiredTokens(currentTimestamp);
        Long activeTokenCount = waitingTokenRepository.getActiveTokenCount();
        long availableSlots = MAX_ACTIVE_TOKENS - (activeTokenCount != null ? activeTokenCount : 0);
        if (availableSlots > 0) {
            Set<String> tokensToActivate = waitingTokenRepository.getTokensFromQueueForActive(availableSlots);
            long expiryTimestamp = currentTimestamp + 1000 * 60 * 30; // 30분
            waitingTokenRepository.activateTokens(tokensToActivate, expiryTimestamp);
        }
    }

    @Override
    public boolean isValid(String token) {
        return waitingTokenRepository.isActiveToken(token);
    }

    @Override
    public Long getUserId(String token) {
        Object userId = waitingTokenRepository.findUserIdByToken(token)
                .orElseThrow(() -> new UserNotFoundException("Invalid token"));
        return Long.parseLong(userId.toString());
    }

}
