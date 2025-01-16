package kr.hhplus.be.server.interfaces.api.token;

import kr.hhplus.be.server.domain.waitingtoken.TokenStatus;
import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenResult;

public record TokenResponse(
        String token,
        TokenStatus status,
        Long position
) {

    public static TokenResponse from(WaitingTokenResult issue) {
        return new TokenResponse(issue.token(), issue.status(), issue.position());
    }
}
