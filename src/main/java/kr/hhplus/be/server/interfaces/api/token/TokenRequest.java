package kr.hhplus.be.server.interfaces.api.token;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
        @NotBlank(message = "전화번호는 필수입력값입니다.")
        String phoneNumber
) {
}
