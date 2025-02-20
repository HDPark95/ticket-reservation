package kr.hhplus.be.server.domain.payment;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.ConcertService;
import kr.hhplus.be.server.domain.concert.Seat;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
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
    private final ApplicationEventPublisher applicationEventPublisher;

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

    private final UserService userService;
    private final ReservationService reservationService;
    private final ConcertService concertService;

    @Override
    @Transactional
    public PaymentResult pay(Long reservationId, Long userId) {
        Reservation reservation = reservationService.getReservation(reservationId, userId);
        Seat seat = concertService.getSeat(reservation.getSeatId());
        User user = userService.usePoint(reservation.getUserId(), seat.getPrice());
        Payment payment = paymentRepository.save(Payment.create(
                user.getId(),
                reservationId,
                seat.getConcertSchedule().getDate(),
                seat.getSeatNumber(),
                seat.getPrice()
        ));
        applicationEventPublisher.publishEvent(new PaymentCompleteEvent(payment.getId()));
        return PaymentResult.from(payment);
    }

    @Override
    @Transactional
    public void putPaymentCompleteMessageToOutbox(Long paymentId) {
        PaymentCompleteOutbox message = PaymentCompleteOutbox.put(paymentId, clock);
        paymentRepository.saveOutbox(message);
    }

    @Override
    @Transactional
    public void updatePaymentCompleteMessageStatus(List<Long> paymentIds, PaymentCompleteOutbox.Status status) {
        paymentRepository.updateOutboxStatus(paymentIds, status);
    }
}
