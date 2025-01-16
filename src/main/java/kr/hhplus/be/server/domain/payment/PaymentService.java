package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.application.payment.PaymentResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    PaymentResult make(PaymentCommand paymentCommand);


    Page<PaymentResult> getPaymentByUserId(Long userId, Pageable pageable);
}
