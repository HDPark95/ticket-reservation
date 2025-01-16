package kr.hhplus.be.server.domain.concert;

import kr.hhplus.be.server.domain.core.NotFoundException;

public class SeatNotFoundException extends NotFoundException {
    public SeatNotFoundException(String message) {
        super(message);
    }
}
