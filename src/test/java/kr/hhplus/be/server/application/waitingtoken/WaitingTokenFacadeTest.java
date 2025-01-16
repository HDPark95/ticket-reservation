package kr.hhplus.be.server.application.waitingtoken;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserNotFoundException;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class WaitingTokenFacadeTest {

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
    @Transactional
    @DisplayName("토큰 발급이 정상적으로 처리된다. - 예외가 발생하지 않는다.")
    void issue() {
        // given
        String phoneNumber = user.getPhoneNumber();

        // when & then
        assertDoesNotThrow(() ->
            waitingTokenFacade.issue(phoneNumber)
        );
    }

    @Test
    @Transactional
    @DisplayName("존재하지 않는 전화번호로 토큰 발급 시도 시 예외가 발생한다.")
    void issueWithNonExistentPhoneNumber() {
        // given
        String phoneNumber = "010-1234-9999";

        // when & then
        assertThrows(UserNotFoundException.class, () ->
            waitingTokenFacade.issue(phoneNumber)
        );
    }

}
