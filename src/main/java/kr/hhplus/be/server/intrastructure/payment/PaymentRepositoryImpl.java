package kr.hhplus.be.server.intrastructure.payment;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJPARepository paymentJpaRepository;
    private final PaymentQuerydslRepository paymentQuerydslRepository;
    private final PaymentRedisRepository paymentRedisRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

    @Override
    public List<Payment> findByUserId(Long userId) {
        return paymentQuerydslRepository.findByUserId(userId);
    }

    @Override
    public List<Payment> findAllByUserId(Long userId, Pageable pageable) {
        return paymentQuerydslRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public Long countByUserId(Long userId) {
        return paymentQuerydslRepository.countByUserId(userId);
    }

    @Override
    public void addFailedPayment(Long reservationId, Long now) {
        paymentRedisRepository.addFailedPayment(reservationId, now);
    }

    @Override
    public List<Long> getFailedPaymentReservationIds() {
        return paymentRedisRepository.getFailedPaymentReservationIds();
    }

    @Override
    public Optional<Payment> findByReservationId(Long reservationId) {
        return paymentQuerydslRepository.findByReservationId(reservationId);
    }
}
