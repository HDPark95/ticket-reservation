package kr.hhplus.be.server.interfaces.api.user;

import java.math.BigDecimal;

public record UserRequest() {
    public static record Add(
            BigDecimal amount
    ){

    }
}
