package kr.hhplus.be.server.domain.waitingtoken;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WaitingTokenRepository {

    WaitingToken save(WaitingToken waitingToken);

    List<WaitingToken> saveAll(List<WaitingToken> tokens);

    Optional<WaitingToken> findByUserIdForUpdate(Long userId);

    Long getPosition(Long userId);

    Optional<WaitingToken> findByUserId(Long userId);

    void deleteExpiredTokens(LocalDateTime now);

    void activateTop100Tokens();

    Optional<WaitingToken> findByToken(String waitingToken);

    void deleteAll();

    List<WaitingToken> findAll();
}
