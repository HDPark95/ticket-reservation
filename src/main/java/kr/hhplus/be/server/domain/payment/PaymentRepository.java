package kr.hhplus.be.server.domain.payment;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentRepository {

    Payment save(Payment payment);

    List<Payment> findByUserId(Long userId);

    List<Payment> findAllByUserId(Long userId, Pageable pageable);

    Long countByUserId(Long userId);

}
