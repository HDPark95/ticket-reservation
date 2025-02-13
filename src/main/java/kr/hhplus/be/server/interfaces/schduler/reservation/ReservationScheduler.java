package kr.hhplus.be.server.interfaces.schduler.reservation;

import kr.hhplus.be.server.application.reservation.ReservationCompleteFailedEvent;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.reservation.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {
    private final ReservationService reservationService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Scheduled(fixedDelay = 10000)
    public void failCompleteReservationPublish() {
        List<Long> reservationIds = reservationService.getFailCompleteReservationIds();
        reservationIds.forEach(reservationId -> applicationEventPublisher.publishEvent(new ReservationCompleteFailedEvent(reservationId)));
    }
}
