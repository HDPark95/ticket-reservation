package kr.hhplus.be.server.intrastructure.payment;

import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJPARepository paymentJpaRepository;
    private final PaymentQuerydslRepository paymentQuerydslRepository;

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
}
