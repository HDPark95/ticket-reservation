package kr.hhplus.be.server.domain.waitingtoken;

public interface WaitingTokenService {
    WaitingTokenResult issue(Long id);

    WaitingTokenResult getWaitingTokenInfo(String token);

    void refreshWaitingTokens();

    boolean isValid(String waitingToken);

    Long getUserId(String waitingToken);

    void expireToken(Long userId);
}
