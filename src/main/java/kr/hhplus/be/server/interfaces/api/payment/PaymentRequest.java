package kr.hhplus.be.server.interfaces.api.payment;

import jakarta.validation.constraints.NotNull;

public record PaymentRequest() {
    public static record pay(
            @NotNull(message = "예약 ID는 필수입력값입니다.")
            Long reservationId
    ){

    }
}
