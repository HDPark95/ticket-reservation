package kr.hhplus.be.server.domain.waitingtoken;

public record WaitingTokenResult(String token,
                                 TokenStatus status,
                                 Long position) {
    public static WaitingTokenResult from(WaitingToken waitingToken, Long position) {
        return new WaitingTokenResult(
                waitingToken.getToken(),
                waitingToken.getStatus(),
                position
        );
    }
}
