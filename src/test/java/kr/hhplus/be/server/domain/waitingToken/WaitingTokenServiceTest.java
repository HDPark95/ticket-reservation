package kr.hhplus.be.server.domain.waitingToken;

import kr.hhplus.be.server.domain.waitingtoken.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class WaitingTokenServiceTest {

    @Autowired
    private WaitingTokenService waitingTokenService;

    @Autowired
    private WaitingTokenRepository waitingTokenRepository;

    @BeforeEach
    void setUp() {
        waitingTokenRepository.deleteAll();
    }

    @Test
    @DisplayName("새로운 유저가 대기열 토큰 발급을 신청하면 새로운 토큰이 발급된다.")
    void issueToken() {
        // given
        Long userId = 1L;

        // when
        WaitingTokenResult result = waitingTokenService.issue(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.token()).isNotNull();
        assertThat(result.status()).isEqualTo(TokenStatus.WAITING);
        assertThat(result.position()).isEqualTo(1L);
    }

    @Test
    @DisplayName("기존 유저가 대기열 토큰 발급을 신청하면 기존 토큰이 반환된다.")
    void issueTokenForExistingUser() {
        // given
        Long userId = 1L;
        WaitingTokenResult existingToken = waitingTokenService.issue(userId);

        // when
        WaitingTokenResult result = waitingTokenService.issue(userId);

        // then
        assertThat(result.token()).isEqualTo(existingToken.token());
    }

    @Test
    @DisplayName("존재하지 않은 토큰 검증 시 WaitingTokenNotFoundException 예외가 발생한다.")
    void invalidTokenThrowsException() {
        // given
        String invalidToken = "invalid-token";

        // when & then
        assertThrows(WaitingTokenNotFoundException.class, () -> waitingTokenService.isValid(invalidToken));
    }
}

