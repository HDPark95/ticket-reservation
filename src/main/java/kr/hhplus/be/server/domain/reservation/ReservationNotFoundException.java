package kr.hhplus.be.server.domain.reservation;

import kr.hhplus.be.server.domain.core.NotFoundException;

public class ReservationNotFoundException extends NotFoundException {
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
