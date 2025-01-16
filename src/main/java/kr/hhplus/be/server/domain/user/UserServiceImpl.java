package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.application.userpoint.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findByIdForUpdate(Long userId) {
        return userRepository.findByIdForUpdate(userId);
    }

    @Override
    public UserResult getUserByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UserNotFoundException("해당하는 사용자가 없습니다."));
        return UserResult.from(user);
    }

    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당하는 사용자가 없습니다."));
    }

    @Override
    public void usePoint(User user, BigDecimal price) {
        user.usePoint(price);
    }
}
