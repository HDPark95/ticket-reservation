package kr.hhplus.be.server.domain.payment;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.payment.PaymentResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResult make(PaymentCommand command) {
        Payment payment = paymentRepository.save(Payment.create(
                command.userId(),
                command.reservationId(),
                command.concertDate(),
                command.seatNumber(),
                command.price()
        ));
        return PaymentResult.from(payment);
    }

    @Override
    public Page<PaymentResult> getPaymentByUserId(Long userId, Pageable pageable) {
        List<Payment> payments = paymentRepository.findAllByUserId(userId, pageable);
        Long totalCount = paymentRepository.countByUserId(userId);
        List<PaymentResult> content = payments.stream().map(PaymentResult::from).toList();
        return new PageImpl<>(content, pageable, totalCount);
    }
}
