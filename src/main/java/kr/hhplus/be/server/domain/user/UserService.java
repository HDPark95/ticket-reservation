package kr.hhplus.be.server.domain.user;

import java.math.BigDecimal;
import java.util.Optional;

public interface UserService {

    Optional<User> findById(Long userId);
    Optional<User> findByIdForUpdate(Long userId);
    UserResult getUserByPhoneNumber(String phoneNumber);

    User getUser(Long userId);

    User usePoint(Long userId, BigDecimal price);

    User addPoint(Long userId, BigDecimal amount);

}
