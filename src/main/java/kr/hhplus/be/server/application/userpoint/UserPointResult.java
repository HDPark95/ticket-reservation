package kr.hhplus.be.server.application.userpoint;

import kr.hhplus.be.server.domain.user.User;

import java.math.BigDecimal;

public record UserPointResult(
        Long userId,
        BigDecimal point
) {
    public static UserPointResult fromUser(User user) {
        return new UserPointResult(user.getId(), user.getPoint());
    }
}
