package kr.hhplus.be.server.application.userpoint;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserPointFacade {

    private final UserService userService;

    @Transactional
    public UserPointResult getPointInfo(Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("해당하는 사용자가 없습니다."));
        return UserPointResult.fromUser(user);
    }

    @Transactional
    public UserPointResult addPoint(Long userId, BigDecimal amount) {
        User user = userService.findByIdForUpdate(userId)
                .orElseThrow(() -> new UserNotFoundException("해당하는 사용자가 없습니다."));

        user.addPoint(amount);
        return UserPointResult.fromUser(user);
    }

    @Transactional
    public UserPointResult usePoint(Long userId, BigDecimal amount) {
        User user = userService.findByIdForUpdate(userId)
                .orElseThrow(() -> new UserNotFoundException("해당하는 사용자가 없습니다."));

        user.usePoint(amount);
        return UserPointResult.fromUser(user);
    }
}
