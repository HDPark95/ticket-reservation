package kr.hhplus.be.server.interfaces.payment;

import kr.hhplus.be.server.application.payment.PaymentFacade;
import kr.hhplus.be.server.application.payment.PaymentResult;
import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    public PaymentFacade paymentFacade;

    @MockitoBean
    private WaitingTokenService waitingTokenService;

    @Test
    @DisplayName("결제 api 호출 - 성공")
    void pay() throws Exception {
        // Given
        Long userId = 1L;
        when(waitingTokenService.isValid(anyString())).thenReturn(true);
        when(waitingTokenService.getUserId(anyString())).thenReturn(userId);
        when(paymentFacade.pay(any())).thenReturn(
                new PaymentResult(1L, BigDecimal.valueOf(10000), LocalDate.parse("2025-01-20"), 1)
        );
        mockMvc.perform(post("/api/v1/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reservationId\":123}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(1L))
                .andExpect(jsonPath("$.price").value(10000))
                .andExpect(jsonPath("$.concertDate").value("2025-01-20"))
                .andExpect(jsonPath("$.seatNumber").value(1));
    }

    @Test
    @DisplayName("결제 api 호출 - 실패 (토큰이 유효하지 않음)")
    void payFail() throws Exception {
        // Given
        when(waitingTokenService.isValid(anyString())).thenReturn(false);

        mockMvc.perform(post("/api/v1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"reservationId\":123}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("결제 api 호출 - 실패 (예약번호 미입력)")
    void payFailReservationIdEmpty() throws Exception {
        // Given
        String invalidRequest = "{\"reservationId\": \"\"}";
        when(waitingTokenService.isValid(anyString())).thenReturn(true);
        mockMvc.perform(post("/api/v1/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }
}
