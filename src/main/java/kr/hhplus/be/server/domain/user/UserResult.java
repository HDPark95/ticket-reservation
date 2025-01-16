package kr.hhplus.be.server.domain.user;

public record UserResult(
        Long id,
        String name,
        String phoneNumber
) {
    public static UserResult from(User user) {
        return new UserResult(
                user.getId(),
                user.getName(),
                user.getPhoneNumber()
        );
    }
}
