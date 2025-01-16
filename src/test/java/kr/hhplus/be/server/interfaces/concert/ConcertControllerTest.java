package kr.hhplus.be.server.interfaces.concert;

import kr.hhplus.be.server.application.concert.ConcertFacade;
import kr.hhplus.be.server.application.concert.ConcertResult;
import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenService;
import kr.hhplus.be.server.interfaces.api.concert.ConcertController;
import kr.hhplus.be.server.interfaces.handler.TokenUserArgumentResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ConcertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConcertFacade concertFacade;

    @MockitoBean
    private WaitingTokenService waitingTokenService;


    @Test
    @DisplayName("공연 일자 조회 - 성공")
    void availableDates() throws Exception {
        Long concertId = 1L;

        List<ConcertResult.ScheduleInfo> schedules = List.of(
                new ConcertResult.ScheduleInfo(101L, LocalDate.of(2025, 1, 20)),
                new ConcertResult.ScheduleInfo(102L, LocalDate.of(2025, 1, 21))
        );

        when(waitingTokenService.isValid(anyString())).thenReturn(true);
        when(waitingTokenService.getUserId(anyString())).thenReturn(1L);
        when(concertFacade.getSchedules(concertId)).thenReturn(schedules);

        mockMvc.perform(get("/api/v1/concerts/{id}/dates", concertId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].concertScheduleId").value(101L))
                .andExpect(jsonPath("$[0].date").value("2025-01-20"))
                .andExpect(jsonPath("$[1].concertScheduleId").value(102L))
                .andExpect(jsonPath("$[1].date").value("2025-01-21"));
    }

    @Test
    @DisplayName("공연 일자 조회 - 토큰 검증 실패 ")
    void availableDatesTokenInvalid() throws Exception {
        Long concertId = 1L;

        List<ConcertResult.ScheduleInfo> schedules = List.of(
                new ConcertResult.ScheduleInfo(101L, LocalDate.of(2025, 1, 20)),
                new ConcertResult.ScheduleInfo(102L, LocalDate.of(2025, 1, 21))
        );

        when(waitingTokenService.isValid(anyString())).thenReturn(false);
        when(waitingTokenService.getUserId(anyString())).thenReturn(1L);
        when(concertFacade.getSchedules(concertId)).thenReturn(schedules);

        mockMvc.perform(get("/api/v1/concerts/{id}/dates", concertId).header("X-Waiting-Token", "invalid"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("공연 좌석 조회 - 성공")
    void availableSeats() throws Exception {
        Long concertScheduleId = 101L;

        List<ConcertResult.SeatInfo> seats = List.of(
                new ConcertResult.SeatInfo(201L, 1, BigDecimal.valueOf(50000)),
                new ConcertResult.SeatInfo(202L, 2, BigDecimal.valueOf(60000))
        );

        when(waitingTokenService.isValid(anyString())).thenReturn(true);
        when(waitingTokenService.getUserId(anyString())).thenReturn(1L);
        when(concertFacade.getAvailableSeats(concertScheduleId)).thenReturn(seats);

        mockMvc.perform(get("/api/v1/concerts/seats").param("concertScheduleId", String.valueOf(concertScheduleId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seatId").value(201L))
                .andExpect(jsonPath("$[0].seatNumber").value(1))
                .andExpect(jsonPath("$[0].price").value(50000))
                .andExpect(jsonPath("$[1].seatId").value(202L))
                .andExpect(jsonPath("$[1].seatNumber").value(2))
                .andExpect(jsonPath("$[1].price").value(60000));
    }

    @Test
    @DisplayName("공연 좌석 조회 - 토큰 검증 실패")
    void availableSeatsTokenInvalid() throws Exception {
        Long concertScheduleId = 101L;

        List<ConcertResult.SeatInfo> seats = List.of(
                new ConcertResult.SeatInfo(201L, 1, BigDecimal.valueOf(50000)),
                new ConcertResult.SeatInfo(202L, 2, BigDecimal.valueOf(60000))
        );

        when(waitingTokenService.isValid(anyString())).thenReturn(false);
        when(waitingTokenService.getUserId(anyString())).thenReturn(1L);
        when(concertFacade.getAvailableSeats(concertScheduleId)).thenReturn(seats);

        mockMvc.perform(get("/api/v1/concerts/seats").param("concertScheduleId", String.valueOf(concertScheduleId)).header("X-Waiting-Token", "invalid"))
                .andExpect(status().isUnauthorized());
    }
}
