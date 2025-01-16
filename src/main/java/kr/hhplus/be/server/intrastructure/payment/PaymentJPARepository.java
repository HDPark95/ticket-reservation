package kr.hhplus.be.server.intrastructure.payment;

import kr.hhplus.be.server.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJPARepository extends JpaRepository<Payment, Long> {

}
