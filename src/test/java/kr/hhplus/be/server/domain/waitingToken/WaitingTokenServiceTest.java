package kr.hhplus.be.server.domain.waitingToken;

import kr.hhplus.be.server.domain.waitingtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class WaitingTokenServiceTest {

    @Autowired
    private WaitingTokenService waitingTokenService;

    @Autowired
    private WaitingTokenRepository waitingTokenRepository;


    @Test
    @DisplayName("새로운 유저가 대기열 토큰 발급을 신청하면 새로운 토큰이 발급된다.")
    void getOrIssueWaitingTokenToken() {
        // given
        Long userId = 1L;

        // when
        WaitingTokenResult result = waitingTokenService.issue(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.token()).isNotNull();
    }

    @Test
    @DisplayName("기존 유저가 대기열 토큰 발급을 신청하면 동일한 토큰이 발급된다.")
    void getOrIssueWaitingTokenTokenForExistingUser() {
        // given
        Long userId = 1L;
        WaitingTokenResult existingToken = waitingTokenService.issue(userId);

        // when
        WaitingTokenResult result = waitingTokenService.issue(userId);

        // then
        assertThat(result.token()).isEqualTo(existingToken.token());
    }

    @Test
    @DisplayName("존재하지 않은 토큰 검증 시 false를 반환한다.")
    void invalidTokenThrowsException() {
        // given
        String invalidToken = "invalid-token";

        // when
        boolean valid = waitingTokenService.isValid(invalidToken);

        //then
        assertThat(valid).isFalse();
    }

    @Test
    @DisplayName("활성화 큐에서 만료된 토큰은 제거된다.")
    void expiredToken() {
        // given
        String expiredToken = UUID.randomUUID().toString();
        waitingTokenRepository.activateTokens(Set.of(expiredToken), System.currentTimeMillis() - 1000 * 60 * 30);

        // when
        waitingTokenService.refreshWaitingTokens();

        // then
        assertThat(waitingTokenService.isValid(expiredToken)).isFalse();
    }

    @Test
    @DisplayName("활성큐에 100개 이하의 토큰이 있는 경우 대기중인 토큰은 활성화 상태로 변경된다.")
    void activateToken() {
        // given
        String waitingToken = UUID.randomUUID().toString();
        waitingTokenRepository.addToWaitingQueue(waitingToken, System.currentTimeMillis());

        // when
        waitingTokenService.refreshWaitingTokens();

        // then
        assertThat(waitingTokenService.isValid(waitingToken)).isTrue();
    }
}

