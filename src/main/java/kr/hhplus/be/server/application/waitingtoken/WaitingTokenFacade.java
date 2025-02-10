package kr.hhplus.be.server.application.waitingtoken;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.user.UserResult;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenResult;
import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenService;
import kr.hhplus.be.server.intrastructure.redis.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingTokenFacade {

    private final WaitingTokenService waitingTokenService;
    private final UserService userService;

    public WaitingTokenResult issue(String phoneNumber) {
        UserResult user = userService.getUserByPhoneNumber(phoneNumber);
        return waitingTokenService.issue(user.id());
    }

    public WaitingTokenResult getWaitingToken(String token) {
        return waitingTokenService.getWaitingTokenInfo(token);
    }
}
