package kr.hhplus.be.server.domain.payment;

import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.application.payment.PaymentResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentService {
    PaymentResult create(PaymentCommand paymentCommand);


    Page<PaymentResult> getPaymentByUserId(Long userId, Pageable pageable);

    List<Long> getFailedPaymentReservationIds();

    void addFailedPayment(Long aLong);

    Payment getPaymentByReservationId(Long reservationId);
}
