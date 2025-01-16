package kr.hhplus.be.server.interfaces.user;

import kr.hhplus.be.server.application.concert.ConcertFacade;
import kr.hhplus.be.server.application.userpoint.UserPointFacade;
import kr.hhplus.be.server.application.userpoint.UserPointResult;
import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserPointFacade userPointFacade;

    @MockitoBean
    private ConcertFacade concertFacade;

    @MockitoBean
    private WaitingTokenService waitingTokenService;

    @Test
    @DisplayName("잔액 조회 - 성공")
    void shouldReturnBalance() throws Exception {
        Long userId = 1L;
        BigDecimal balance = new BigDecimal("10000");

        when(waitingTokenService.isValid(anyString())).thenReturn(true);
        when(waitingTokenService.getUserId(anyString())).thenReturn(userId);
        when(userPointFacade.getPointInfo(userId)).thenReturn(new UserPointResult(userId, balance));

        mockMvc.perform(get("/api/v1/users/balance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("10000"));
    }

    @Test
    @DisplayName("잔액 조회 - 실패 (토큰이 유효하지 않음)")
    void shouldReturnBadRequestForInvalidToken() throws Exception {
        when(waitingTokenService.isValid(anyString())).thenReturn(false);

        mockMvc.perform(get("/api/v1/users/balance"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("잔액 추가 api 호출 - 성공")
    void shouldAddBalance() throws Exception {
        Long userId = 1L;
        BigDecimal balance = new BigDecimal("10000");
        BigDecimal amount = new BigDecimal("1000");

        when(waitingTokenService.isValid(anyString())).thenReturn(true);
        when(waitingTokenService.getUserId(anyString())).thenReturn(userId);
        when(userPointFacade.addPoint(userId, amount)).thenReturn(new UserPointResult(userId, balance.add(amount)));

        mockMvc.perform(post("/api/v1/users/balance")
                        .contentType("application/json")
                        .content("{\"amount\": 1000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("11000"));
    }

    @Test
    @DisplayName("잔액 추가 api 호출 - 실패 (토큰이 유효하지 않음)")
    void addBalanceShouldReturnBadRequestForInvalidToken() throws Exception {
        when(waitingTokenService.isValid(anyString())).thenReturn(false);

        mockMvc.perform(post("/api/v1/users/balance")
                .contentType("application/json")
                .content("{\"amount\": 1000}"))
                .andExpect(status().isUnauthorized());
    }
}
