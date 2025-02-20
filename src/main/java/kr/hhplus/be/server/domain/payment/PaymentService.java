package kr.hhplus.be.server.domain.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentService {

    Page<PaymentResult> getPaymentByUserId(Long userId, Pageable pageable);

    List<Long> getFailedPaymentReservationIds();

    void addFailedPayment(Long aLong);

    Payment getPaymentByReservationId(Long reservationId);

    PaymentResult pay(Long reservationId, Long userId);

    void putPaymentCompleteMessageToOutbox(Long paymentId);

    void updatePaymentCompleteMessageStatus(List<Long> paymentIds, PaymentCompleteOutbox.Status status);
}
