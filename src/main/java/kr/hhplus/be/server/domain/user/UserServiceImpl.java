package kr.hhplus.be.server.domain.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    @Transactional
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    @Transactional
    public Optional<User> findByIdForUpdate(Long userId) {
        return userRepository.findByIdForUpdate(userId);
    }

    @Override
    @Transactional
    public UserResult getUserByPhoneNumber(String phoneNumber) {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UserNotFoundException("해당하는 사용자가 없습니다."));
        return UserResult.from(user);
    }

    @Override
    @Transactional
    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당하는 사용자가 없습니다."));
    }

    @Override
    @Transactional
    public User usePoint(Long userId, BigDecimal price) {
        User user = userRepository.findByIdForUpdate(userId)
                .orElseThrow(() -> new UserNotFoundException("해당하는 사용자가 없습니다."));
        user.usePoint(price);
        return user;
    }

    @Override
    @Transactional
    public User addPoint(Long userId, BigDecimal amount) {
        User user = userRepository.findByIdForUpdate(userId)
                .orElseThrow(() -> new UserNotFoundException("해당하는 사용자가 없습니다."));
        user.addPoint(amount);
        return user;
    }

}
