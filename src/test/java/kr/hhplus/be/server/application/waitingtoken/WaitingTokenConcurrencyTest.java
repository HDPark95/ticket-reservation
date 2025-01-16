package kr.hhplus.be.server.application.waitingtoken;

import jakarta.persistence.EntityManager;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.waitingtoken.WaitingToken;
import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenRepository;
import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class WaitingTokenConcurrencyTest {

    @Autowired
    private WaitingTokenFacade waitingTokenFacade;

    @Autowired
    private WaitingTokenRepository waitingTokenRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager em;

    User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder().phoneNumber("010-1234-5678").build());
    }

    @AfterEach
    void tearDown() {
        waitingTokenRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("같은 유저가 동시에 토큰 발급 시도 시 토큰이 중복 발급되지 않는다.")
    void issueConcurrently() throws InterruptedException {
        // given
        String phoneNumber = user.getPhoneNumber();
        Callable<WaitingTokenResult> issueToken = () -> waitingTokenFacade.issue(phoneNumber);
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        List<Callable<WaitingTokenResult>> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tasks.add(issueToken);
        }

        executorService.invokeAll(tasks);

        // when
        List<WaitingToken> tokens = waitingTokenRepository.findAll();
        assertEquals(tokens.size(),1);
    }

}
