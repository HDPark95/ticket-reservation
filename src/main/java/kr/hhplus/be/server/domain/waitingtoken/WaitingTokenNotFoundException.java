package kr.hhplus.be.server.domain.waitingtoken;

import kr.hhplus.be.server.domain.core.NotFoundException;

public class WaitingTokenNotFoundException extends NotFoundException {
    public WaitingTokenNotFoundException(String message) {
        super(message);
    }
}
