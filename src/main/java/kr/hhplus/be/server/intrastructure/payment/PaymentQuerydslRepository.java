package kr.hhplus.be.server.intrastructure.payment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.domain.payment.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.hhplus.be.server.domain.payment.QPayment.payment;

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
}
