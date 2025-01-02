package kr.hhplus.be.server.interfaces.api.token;

public record TokenRequest(
        String username,
        String phoneNumber
) {
}
