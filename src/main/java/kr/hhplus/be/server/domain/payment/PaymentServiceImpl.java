package kr.hhplus.be.server.domain.payment;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.payment.PaymentResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final Clock clock;

    @Override
    @Transactional
    public PaymentResult create(PaymentCommand command) {
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
    @Transactional
    public Page<PaymentResult> getPaymentByUserId(Long userId, Pageable pageable) {
        List<Payment> payments = paymentRepository.findAllByUserId(userId, pageable);
        Long totalCount = paymentRepository.countByUserId(userId);
        List<PaymentResult> content = payments.stream().map(PaymentResult::from).toList();
        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public List<Long> getFailedPaymentReservationIds() {
        return paymentRepository.getFailedPaymentReservationIds();
    }

    @Override
    public void addFailedPayment(Long reservationId) {
        paymentRepository.addFailedPayment(reservationId, Instant.now(clock).toEpochMilli());
    }

    @Override
    public Payment getPaymentByReservationId(Long reservationId) {
        return paymentRepository.findByReservationId(reservationId)
                .orElseThrow(() -> new PaymentNotFoundException("결제 내역이 없습니다."));
    }
}
