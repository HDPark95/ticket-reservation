package kr.hhplus.be.server.application.userpoint;

import kr.hhplus.be.server.domain.user.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class UserPointFacadeTest {

    @Autowired
    private UserPointFacade userPointFacade;

    @Autowired
    private UserRepository userRepository;

    User user;

    @BeforeEach
    public void setUp() {
        // 사용자 생성
        user = userRepository.save(User.builder()
                .name("박현두")
                .phoneNumber("01012341234")
                .point(BigDecimal.ZERO)
                .build());
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("포인트 적립 성공")
    public void addPoint() {
        // given
        Long userId = user.getId();
        BigDecimal amount = new BigDecimal(1000);

        // when
        UserPointResult pointInfo = userPointFacade.addPoint(userId, amount);

        // then

        Assertions.assertTrue(pointInfo.point().compareTo(amount) == 0);
    }

    @Test
    @DisplayName("포인트 조회 성공")
    public void getPointInfo() {
        // given
        Long userId = user.getId();

        // when
        UserPointResult pointInfo = userPointFacade.getPointInfo(userId);

        // then
        Assertions.assertTrue(pointInfo.point().compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    @DisplayName("포인트 사용 성공")
    public void usePoint() {
        // given
        Long userId = user.getId();
        BigDecimal amount = new BigDecimal(1000);
        userPointFacade.addPoint(userId, amount);

        // when
        UserPointResult pointInfo = userPointFacade.usePoint(userId, amount);

        // then
        Assertions.assertTrue(pointInfo.point().compareTo(BigDecimal.ZERO) == 0);
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 포인트를 충전 시도하면 UserNotFoundException 발생")
    public void addPointWithNonExistingUser() {
        // given
        Long userId = 100L;
        BigDecimal amount = new BigDecimal(1000);

        // when & then
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userPointFacade.addPoint(userId, amount);
        });
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 포인트를 조회 시도하면 UserNotFoundException 발생")
    public void getPointInfoWithNonExistingUser() {
        // given
        Long userId = 100L;

        // when & then
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userPointFacade.getPointInfo(userId);
        });
    }

    @Test
    @DisplayName("존재하지 않는 사용자의 포인트를 사용 시도하면 UserNotFoundException 발생")
    public void usePointWithNonExistingUser() {
        // given
        Long userId = 100L;
        BigDecimal amount = new BigDecimal(1000);

        // when & then
        Assertions.assertThrows(UserNotFoundException.class, () -> {
            userPointFacade.usePoint(userId, amount);
        });
    }

    @Test
    @DisplayName("포인트 사용 시 포인트가 부족하면 InsufficientPointsException 발생")
    public void usePointWithInsufficientPoints() {
        // given
        Long userId = user.getId();
        BigDecimal amount = new BigDecimal(1000);

        // when & then
        Assertions.assertThrows(InsufficientPointsException.class, () -> {
            userPointFacade.usePoint(userId, amount);
        });
    }

    @Test
    @DisplayName("포인트 사용 시 음수 포인트를 사용하려고 하면 NegativePointException 발생")
    public void useNegativePoint() {
        // given
        Long userId = user.getId();
        BigDecimal amount = new BigDecimal(-1000);

        // when & then
        Assertions.assertThrows(NegativePointException.class, () -> {
            userPointFacade.usePoint(userId, amount);
        });
    }

    @Test
    @DisplayName("포인트 적립 시 음수 포인트를 적립하려고 하면 NegativePointException 발생")
    public void addNegativePoint() {
        // given
        Long userId = user.getId();
        BigDecimal amount = new BigDecimal(-1000);

        // when & then
        Assertions.assertThrows(NegativePointException.class, () -> {
            userPointFacade.addPoint(userId, amount);
        });
    }

}
