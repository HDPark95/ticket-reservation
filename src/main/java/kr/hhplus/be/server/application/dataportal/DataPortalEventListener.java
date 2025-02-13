package kr.hhplus.be.server.application.dataportal;

import kr.hhplus.be.server.application.payment.PaymentCompleteEvent;
import kr.hhplus.be.server.intrastructure.dataportal.DataPortalAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class DataPortalEventListener {

    private final DataPortalAdapter dataPortalAdapter;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReservationEvent(PaymentCompleteEvent paymentCompleteEvent) {
        dataPortalAdapter.sendReservationInfo(paymentCompleteEvent.getReservationInfo());
    }
}
