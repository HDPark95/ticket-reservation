package kr.hhplus.be.server.application.userpoint;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
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
public class UserPointConcurrencyTest {

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
                .phoneNumber("0101234111234")
                .point(BigDecimal.ZERO)
                .build());
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    private class UseJob implements Callable<UserPointResult> {
        private long userId;
        private BigDecimal amount;

        public UseJob(long userId, BigDecimal amount) {
            this.userId = userId;
            this.amount = amount;
        }

        @Override
        public UserPointResult call() {
            return userPointFacade.usePoint(userId, amount);
        }
    }
    private class ChargeJob implements Callable<UserPointResult> {
        private long userId;
        private BigDecimal amount;

        public ChargeJob(long userId, BigDecimal amount) {
            this.userId = userId;
            this.amount = amount;
        }

        @Override
        public UserPointResult call() {
            return userPointFacade.addPoint(userId, amount);
        }
    }

    @Test
    @DisplayName("같은 회원이 동시에 1000원씩 2번 포인트를 적립하면 한번만 성공하여 1000원이 적립되어야 한다.")
    public void addPointConcurrent() throws InterruptedException {
        // given
        Long userId = user.getId();
        BigDecimal amount = new BigDecimal(1000);

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<ChargeJob> chargeJobs = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            chargeJobs.add(new ChargeJob(userId, amount));
        }
        // when
        executorService.invokeAll(chargeJobs);

        // then
        UserPointResult pointInfo = userPointFacade.getPointInfo(userId);
        Assertions.assertTrue(pointInfo.point().compareTo(new BigDecimal(1000)) == 0);
    }

    @Test
    @DisplayName("같은 회원이 동시에 1000원씩 2번 포인트를 사용하면 한번만 성공하여 1000원이 차감되어야 한다.")
    public void usePointConcurrent() throws InterruptedException {
        // given
        Long userId = user.getId();
        BigDecimal amount = new BigDecimal(1000);
        userPointFacade.addPoint(userId, new BigDecimal(10000));

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<UseJob> useJobs = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            useJobs.add(new UseJob(userId, amount));
        }
        // when
        executorService.invokeAll(useJobs);

        // then
        UserPointResult pointInfo = userPointFacade.getPointInfo(userId);
        Assertions.assertTrue(pointInfo.point().compareTo(BigDecimal.valueOf(9000)) == 0);
    }
}
