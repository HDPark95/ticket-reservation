package kr.hhplus.be.server.domain.reservation;

public class AlreadyPaidReservationException extends RuntimeException{
    public AlreadyPaidReservationException(String message) {
        super(message);
    }
}
