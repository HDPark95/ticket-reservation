package kr.hhplus.be.server.domain.waitingtoken;

public record WaitingTokenResult(String token, Long position) {
    public static WaitingTokenResult from(String token, Long position) {
        return new WaitingTokenResult(
                token,
                position
        );
    }
}
