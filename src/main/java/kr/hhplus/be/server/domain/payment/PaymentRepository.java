package kr.hhplus.be.server.domain.payment;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);

    List<Payment> findByUserId(Long userId);

    List<Payment> findAllByUserId(Long userId, Pageable pageable);

    Long countByUserId(Long userId);

    void addFailedPayment(Long reservationId, Long now);

    List<Long> getFailedPaymentReservationIds();

    Optional<Payment> findByReservationId(Long reservationId);

    void saveOutbox(PaymentCompleteOutbox message);

    void updateOutboxStatus(List<Long> paymentIds, PaymentCompleteOutbox.Status status);

    List<Long> getPendingPaymentIds();

}
