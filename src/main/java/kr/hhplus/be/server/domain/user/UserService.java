package kr.hhplus.be.server.domain.user;

import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long userId);

    Optional<User> findByIdForUpdate(Long userId);
    UserResult getUserByPhoneNumber(String phoneNumber);
}
