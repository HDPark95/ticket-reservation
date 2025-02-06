package kr.hhplus.be.server.interfaces.api.token;

import kr.hhplus.be.server.domain.waitingtoken.TokenStatus;
import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenResult;

public record TokenResponse(
        String token,
        TokenStatus status,
        Long position
) {

    public static TokenResponse from(WaitingTokenResult issue) {
        TokenStatus status = TokenStatus.WAITING;
        if (issue.position() == 0){
            status = TokenStatus.ACTIVATE;
        }
        return new TokenResponse(issue.token(), status, issue.position());
    }
}
