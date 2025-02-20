package kr.hhplus.be.server.infrastructure.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static kr.hhplus.be.server.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<User> findByIdForUpdate(Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(user)
                        .where(user.id.eq(id))
                        .fetchOne()
        );
    }

    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(user)
                        .where(user.phoneNumber.eq(phoneNumber))
                        .fetchOne()
        );
    }
}
