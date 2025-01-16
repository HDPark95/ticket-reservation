package kr.hhplus.be.server.interfaces.waitingtoken;

import kr.hhplus.be.server.application.waitingtoken.WaitingTokenFacade;
import kr.hhplus.be.server.domain.waitingtoken.TokenStatus;
import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class WaitingTokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WaitingTokenFacade waitingTokenFacade;

    @Test
    @DisplayName("토큰 발급 - 성공")
    void tokenIssue() throws Exception {
        // Given
        String phoneNumber = "01012345678";
        String token = "sample-token";
        TokenStatus status = TokenStatus.ACTIVATE;
        Long position = 1L;

        WaitingTokenResult result = new WaitingTokenResult(token, status, position);

        when(waitingTokenFacade.issue(any(String.class))).thenReturn(result);

        // When & Then
        mockMvc.perform(post("/api/v1/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phoneNumber\": \"" + phoneNumber + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.status").value(status.name()))
                .andExpect(jsonPath("$.position").value(position));
    }

    @Test
    @DisplayName("전화번호 미입력으로 토큰 발급 실패 - validationExcpetion 발생")
    void phoneNumberEmptyFail() throws Exception {
        String invalidRequest = "{\"phoneNumber\": \"\"}";

        // When & Then
        mockMvc.perform(post("/api/v1/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }
}
