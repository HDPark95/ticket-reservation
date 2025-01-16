package kr.hhplus.be.server.domain.waitingtoken;

public interface WaitingTokenService {
    WaitingTokenResult issue(Long userId);

    void refreshWaitingTokens();

    boolean isValid(String waitingToken);

    Long getUserId(String waitingToken);
}
