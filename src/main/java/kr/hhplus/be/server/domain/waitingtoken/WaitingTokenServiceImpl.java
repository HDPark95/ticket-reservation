package kr.hhplus.be.server.domain.waitingtoken;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WaitingTokenServiceImpl implements WaitingTokenService {

    private final WaitingTokenRepository waitingTokenRepository;
    private final Clock clock;

    @Override
    @Transactional
    public WaitingTokenResult issue(Long userId) {
        Optional<WaitingToken> optionalWaitingToken = waitingTokenRepository.findByUserIdForUpdate(userId);

        WaitingToken waitingToken;
        if (optionalWaitingToken.isEmpty()) {
            waitingToken = WaitingToken.issue(userId, clock);
            waitingTokenRepository.save(waitingToken);
        }else{
            waitingToken = optionalWaitingToken.get();
        }
        /* 현재 대기열 순위 : 내 앞에 있는 WAITING 사람 수 */
        Long position = waitingTokenRepository.getPosition(userId);
        return WaitingTokenResult.from(waitingToken, position);
    }

    @Override
    @Transactional
    public void refreshWaitingTokens() {
        LocalDateTime now = LocalDateTime.now(clock);
        waitingTokenRepository.deleteExpiredTokens(now);
        waitingTokenRepository.activateTop100Tokens();
    }

    @Override
    @Transactional
    public boolean isValid(String waitingToken) {
        WaitingToken token = waitingTokenRepository.findByToken(waitingToken)
                .orElseThrow(() -> new WaitingTokenNotFoundException("유효하지 않은 토큰입니다."));
        return token.validate(clock);
    }
}
