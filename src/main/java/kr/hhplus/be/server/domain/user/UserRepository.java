package kr.hhplus.be.server.domain.user;


import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByIdForUpdate(Long id);

    Optional<User> findById(Long id);

    void deleteAll();

    Optional<User> findByPhoneNumber(String phoneNumber);

}
