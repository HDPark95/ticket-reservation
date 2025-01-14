package kr.hhplus.be.server.infrastucture.waitingtoken;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.waitingtoken.TokenStatus;
import kr.hhplus.be.server.domain.waitingtoken.WaitingToken;
import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class WaitingTokenRepositoryTest {

    @Autowired
    private WaitingTokenRepository waitingTokenRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        waitingTokenRepository.deleteAll();
        em.flush();
    }

    @Test
    @DisplayName("만료된 토큰이 정상적으로 삭제된다")
    void deleteExpiredTokens() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 1, 1, 14, 0, 0);

        Clock expiredClock = Clock.fixed(Instant.parse("2025-01-01T12:00:00Z"), ZoneId.systemDefault());
        List<WaitingToken> tokens = new ArrayList<>();
        // 만료된 토큰 생성
        for (Long i = 0L; i < 15L; i+=1L) {
            tokens.add(WaitingToken.issue(i, expiredClock));
        }
        waitingTokenRepository.saveAll(tokens);

        // when
        waitingTokenRepository.deleteExpiredTokens(now);

        // then
        List<WaitingToken> remainingTokens = waitingTokenRepository.findAll();
        assertThat(remainingTokens.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("100개 이내의 대기열 토큰이 정상적으로 활성화 된다.")
    void activateToken() {
        // given
        Clock fixedClock = Clock.fixed(Instant.parse("2025-01-01T12:00:00Z"), ZoneId.systemDefault());
        List<WaitingToken> tokens = new ArrayList<>();
        for (Long i = 0L; i < 15L; i+=1L) {
            tokens.add(WaitingToken.issue(i, fixedClock));
        }
        waitingTokenRepository.saveAll(tokens);
        //영속성 컨텍스트 초기화 : native query를 사용하여 update 하기 때문에 검증부에서 다시 조회 할 수 있게 해야함.
        em.flush();
        em.clear();

        // when
        waitingTokenRepository.activateTop100Tokens();

        // then
        List<WaitingToken> activatedTokens = waitingTokenRepository.findAll();

        assertThat(activatedTokens)
                .allMatch(token -> token.getStatus() == TokenStatus.ACTIVATE);
    }

    @Test
    @DisplayName("50개의 활성토큰과 3개의 대기토큰이 있는 경우 53번째 토큰은 대기열 순번을 3으로 반환한다.")
    void getCurrentPosition() {
        // given
        Clock fixedClock = Clock.fixed(Instant.parse("2025-01-01T12:00:00Z"), ZoneId.systemDefault());
        for (Long i = 1L; i <= 50L; i++) { // 50개의 활성 토큰
            em.createNativeQuery("""
                    INSERT INTO tb_waiting_token (user_id, status, expired_at, created_at, updated_at)
                    VALUES (:user_id, :status, :expiredAt, now(), now())
                """)
                    .setParameter("user_id", i)
                    .setParameter("status", "ACTIVATE")
                    .setParameter("expiredAt", LocalDateTime.now(fixedClock).plusMinutes(30))
                    .executeUpdate();
        }
        for (Long i = 51L; i <= 53L; i++) { // 3개의 대기열 토큰
            em.createNativeQuery("""
                    INSERT INTO tb_waiting_token (user_id, status, expired_at, created_at, updated_at)
                    VALUES (:user_id, :status, :expiredAt, now(), now())
                """)
                    .setParameter("user_id", i)
                    .setParameter("status", "WAITING")
                    .setParameter("expiredAt", LocalDateTime.now(fixedClock).plusMinutes(30))
                    .executeUpdate();
        }

        // when
        Long position = waitingTokenRepository.getPosition(53L);

        // then
        assertThat(position).isEqualTo(3L);
    }
}
