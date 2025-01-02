package kr.hhplus.be.server.interfaces.api.user;

import java.math.BigDecimal;

public record UserResponse() {
    public static record Balance(
            BigDecimal balance
    ) {

    }
}
