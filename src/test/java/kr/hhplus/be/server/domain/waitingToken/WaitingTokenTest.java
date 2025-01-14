package kr.hhplus.be.server.domain.waitingToken;

import kr.hhplus.be.server.domain.waitingtoken.TokenStatus;
import kr.hhplus.be.server.domain.waitingtoken.WaitingToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class WaitingTokenTest {

    @Test
    @DisplayName("토큰 발급 성공 테스트 - 대기 상태, 만료시간 30분")
    void issueWaitingToken() {
        // given
        Long userId = 1L;
        LocalDateTime fixedNow = LocalDateTime.of(2025, 1, 1, 12, 0, 0);
        Clock fixedClock = Clock.fixed(Instant.parse("2025-01-01T12:00:00Z"), ZoneId.of("UTC"));

        // when
        WaitingToken waitingToken = WaitingToken.issue(userId, fixedClock);

        // then
        assertThat(waitingToken.getStatus())
                .as("발급 후 대기 상태 인가")
                .isEqualTo(TokenStatus.WAITING);

        assertThat(waitingToken.getExpiredAt())
                .as("만료시간은 발급시간으로부터 30분 이후 인가")
                .isEqualTo(fixedNow.plusMinutes(30));
    }

    @Test
    @DisplayName("토큰을 활성화 - 활성 상태로 변경")
    void active(){
        // given
        Long userId = 1L;
        Clock fixedClock = Clock.fixed(Instant.parse("2025-01-01T12:00:00Z"), ZoneId.of("UTC"));
        WaitingToken waitingToken = WaitingToken.issue(userId, fixedClock);

        // when
        waitingToken.active();

        // then
        assertThat(waitingToken.getStatus())
                .as("활성화 상태 인가")
                .isEqualTo(TokenStatus.ACTIVATE);
    }

    @Test
    @DisplayName("토큰 검증 성공 테스트 - 만료시간 이전 검증 & 활성화 상태")
    void validate() {
        // given
        Long userId = 1L;
        Clock nowClock = Clock.fixed(Instant.parse("2025-01-01T12:20:00Z"), ZoneId.of("UTC"));
        Clock fixedClock = Clock.fixed(Instant.parse("2025-01-01T12:00:00Z"), ZoneId.of("UTC"));
        WaitingToken waitingToken = WaitingToken.issue(userId, fixedClock);
        waitingToken.active();

        // when
        boolean isValid = waitingToken.validate(nowClock);

        // then
        assertThat(isValid)
                .isTrue();
    }

    @Test
    @DisplayName("토큰 검증 만료시간 초과 검증 실패 테스트 - 만료시간 이후 검증 & 활성화 상태")
    void validateExpired() {
        // given
        Long userId = 1L;
        Clock nowClock = Clock.fixed(Instant.parse("2025-01-01T12:40:00Z"), ZoneId.of("UTC"));
        Clock fixedClock = Clock.fixed(Instant.parse("2025-01-01T12:00:00Z"), ZoneId.of("UTC"));
        WaitingToken waitingToken = WaitingToken.issue(userId, fixedClock);
        waitingToken.active();

        // when
        boolean isValid = waitingToken.validate(nowClock);

        // then
        assertThat(isValid)
                .isFalse();
    }

    @Test
    @DisplayName("토큰 검증 활성화 상태가 아닌 경우 검증 실패 테스트 - 만료시간 이전 검증 & 대기 상태")
    void validateNotActivated() {
        // given
        Long userId = 1L;
        Clock nowClock = Clock.fixed(Instant.parse("2025-01-01T12:20:00Z"), ZoneId.of("UTC"));
        Clock fixedClock = Clock.fixed(Instant.parse("2025-01-01T12:00:00Z"), ZoneId.of("UTC"));
        WaitingToken waitingToken = WaitingToken.issue(userId, fixedClock);

        // when
        boolean isValid = waitingToken.validate(nowClock);

        // then
        assertThat(isValid)
                .isFalse();
    }
}
