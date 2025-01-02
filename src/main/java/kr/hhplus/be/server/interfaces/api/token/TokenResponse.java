package kr.hhplus.be.server.interfaces.api.token;

public record TokenResponse(
        String token,
        String status,
        Long position
) {
}
