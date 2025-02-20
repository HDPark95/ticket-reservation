package kr.hhplus.be.server.intrastructure.payment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentCompleteOutbox;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static kr.hhplus.be.server.domain.payment.QPayment.payment;
import static kr.hhplus.be.server.domain.payment.QPaymentCompleteOutbox.paymentCompleteOutbox;
import static kr.hhplus.be.server.domain.reservation.QReservation.reservation;
import static kr.hhplus.be.server.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class PaymentQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public List<Payment> findByUserId(Long userId) {
        return queryFactory.selectFrom(payment)
                .where(payment.user.id.eq(userId))
                .fetch();
    }

    public List<Payment> findAllByUserId(Long userId, Pageable pageable) {
        return queryFactory.selectFrom(payment)
                .where(payment.user.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public Long countByUserId(Long userId) {
        return queryFactory.select(payment.count()).from(payment)
                .where(payment.user.id.eq(userId))
                .fetchOne();
    }

    public Optional<Payment> findByReservationId(Long reservationId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(payment)
                        .innerJoin(payment.reservation, reservation).fetchJoin()
                        .innerJoin(payment.user, user).fetchJoin()
                        .where(reservation.id.eq(reservationId))
                        .fetchOne()
        );
    }

    public void updateOutboxStatus(List<Long> paymentIds, PaymentCompleteOutbox.Status status) {
        queryFactory.update(paymentCompleteOutbox)
                .set(paymentCompleteOutbox.status, status)
                .where(paymentCompleteOutbox.id.in(paymentIds))
                .execute();
    }

    public List<Long> getPendingPaymentIds() {
        return queryFactory.select(paymentCompleteOutbox.id)
                .from(paymentCompleteOutbox)
                .where(paymentCompleteOutbox.status.eq(PaymentCompleteOutbox.Status.PENDING))
                .fetch();
    }
}
