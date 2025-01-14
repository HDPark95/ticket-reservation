package kr.hhplus.be.server.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {
    @Test
    @DisplayName("사용자 포인트 충전이 정상적으로 충전된다.")
    public void chargePoint() {
        // given
        User user = new User(1L, "test", "01012341234", BigDecimal.ZERO);

        // when
        user.addPoint(BigDecimal.valueOf(1000));

        // then
        assert user.getPoint().equals(BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("사용자 포인트 사용이 정상적으로 차감된다.")
    public void usePoint() {
        // given
        User user = new User(1L, "test", "01012341234", BigDecimal.valueOf(1000));

        // when
        user.usePoint(BigDecimal.valueOf(500));

        // then
        assert user.getPoint().equals(BigDecimal.valueOf(500));
    }

    @Test
    @DisplayName("사용자 포인트가 부족할 경우 InsufficientPointsException 예외가 발생한다.")
    public void usePointWithInsufficientPoint() {
        // given
        User user = new User(1L, "test", "01012341234", BigDecimal.valueOf(1000));

        // when & then
        assertThrows(InsufficientPointsException.class, () -> user.usePoint(BigDecimal.valueOf(2000)));
    }

    @Test
    @DisplayName("사용자가 음수의 포인트를 충전하려고 할 경우 NegativePointException 예외가 발생한다.")
    public void chargeNegativePoint() {
        // given
        User user = new User(1L, "test", "01012341234", BigDecimal.ZERO);

        // when & then
        assertThrows(NegativePointException.class, () -> user.addPoint(BigDecimal.valueOf(-1000)));
    }

    @Test
    @DisplayName("사용자가 음수의 포인트를 사용하려고 할 경우 NegativePointException 예외가 발생한다.")
    public void useNegativePoint() {
        // given
        User user = new User(1L, "test", "01012341234", BigDecimal.valueOf(1000));

        // when & then
        assertThrows(NegativePointException.class, () -> user.usePoint(BigDecimal.valueOf(-500)));
    }
}
