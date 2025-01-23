package kr.hhplus.be.server.application.userpoint;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserNotFoundException;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.intrastructure.redis.DistributedLock;
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

    @DistributedLock(key="'point:' + #userId")
    @Transactional
    public UserPointResult addPoint(Long userId, BigDecimal amount) {
        User user = userService.addPoint(userId, amount);
        return UserPointResult.fromUser(user);
    }

    @DistributedLock(key="'point:' + #userId")
    @Transactional
    public UserPointResult usePoint(Long userId, BigDecimal amount) {
        User user = userService.usePoint(userId, amount);
        return UserPointResult.fromUser(user);
    }
}
