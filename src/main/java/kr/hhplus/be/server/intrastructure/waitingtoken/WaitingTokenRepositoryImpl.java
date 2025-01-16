package kr.hhplus.be.server.intrastructure.waitingtoken;

import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.user.QUser;
import kr.hhplus.be.server.domain.waitingtoken.TokenStatus;
import kr.hhplus.be.server.domain.waitingtoken.WaitingToken;
import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static kr.hhplus.be.server.domain.user.QUser.user;
import static kr.hhplus.be.server.domain.waitingtoken.QWaitingToken.waitingToken;

@Repository
@RequiredArgsConstructor
public class WaitingTokenRepositoryImpl implements WaitingTokenRepository {

    private final WaitingTokenJPARepository waitingTokenJPARepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public WaitingToken save(WaitingToken waitingToken) {
        return waitingTokenJPARepository.save(waitingToken);
    }

    @Override
    public List<WaitingToken> saveAll(List<WaitingToken> tokens){
        return waitingTokenJPARepository.saveAll(tokens);
    }

    @Override
    public Optional<WaitingToken> findByUserIdForUpdate(Long userId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(waitingToken)
                .where(
                        waitingToken.user.id.eq(userId)
                )
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne());
    }

    @Override
    public Long getPosition(Long userId) {
        return jpaQueryFactory
                .select(waitingToken.count().add(1))
                .from(waitingToken)
                .where(
                        waitingToken.status.eq(TokenStatus.WAITING),
                        waitingToken.id.lt(
                                jpaQueryFactory
                                        .select(waitingToken.id)
                                        .from(waitingToken)
                                        .where(waitingToken.user.id.eq(userId))
                        )
                ).fetchOne();
    }

    @Override
    public Optional<WaitingToken> findByUserId(Long userId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(waitingToken)
                .where(
                        waitingToken.user.id.eq(userId)
                )
                .fetchOne());
    }

    @Override
    public void deleteExpiredTokens(LocalDateTime now) {
        jpaQueryFactory
                .delete(waitingToken)
                .where(
                        waitingToken.expiredAt.lt(now)
                )
                .execute();
    }

    @Override
    public void activateTop100Tokens() {
        waitingTokenJPARepository.activateTop100Tokens();
    }

    @Override
    public Optional<WaitingToken> findByToken(String token) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(waitingToken)
                .innerJoin(waitingToken.user, user).fetchJoin()
                .where(
                        waitingToken.token.eq(token)
                )
                .fetchOne());
    }

    @Override
    public void deleteAll() {
        waitingTokenJPARepository.deleteAll();
    }

    @Override
    public List<WaitingToken> findAll() {
        return waitingTokenJPARepository.findAll();
    }
}
