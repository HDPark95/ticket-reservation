package kr.hhplus.be.server.interfaces.schduler.waitingtoken;

import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WaitingTokenScheduler {

    private final WaitingTokenService tokenService;

    @Scheduled(fixedDelay = 10000)
    public void checkWaitingToken() {
        tokenService.refreshWaitingTokens();
    }
}
