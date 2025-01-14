package kr.hhplus.be.server.domain.waitingtoken;

public class WaitingTokenNotFoundException extends RuntimeException {
    public WaitingTokenNotFoundException(String message) {
        super(message);
    }
}
