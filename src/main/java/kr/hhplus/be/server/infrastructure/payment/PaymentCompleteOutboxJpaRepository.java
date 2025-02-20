package kr.hhplus.be.server.infrastructure.payment;

import kr.hhplus.be.server.domain.payment.PaymentCompleteOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentCompleteOutboxJpaRepository extends JpaRepository<PaymentCompleteOutbox, Long> {
}
