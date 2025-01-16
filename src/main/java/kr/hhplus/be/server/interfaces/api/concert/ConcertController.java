package kr.hhplus.be.server.interfaces.api.concert;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.application.concert.ConcertFacade;
import kr.hhplus.be.server.application.concert.ConcertResult;
import kr.hhplus.be.server.interfaces.api.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/concerts")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertFacade concertFacade;

    @GetMapping(path="/{id}/dates", produces = { "application/json" })
    @Operation(summary = "공연 일자 조회", description = "예약 가능한 일자를 조회합니다.", tags={ "concerts" })
    public ResponseEntity<List<ConcertResponse.AvailableDate>> date(@PathVariable("id") Long concertId) {
        List<ConcertResult.ScheduleInfo> schedules = concertFacade.getSchedules(concertId);
        List<ConcertResponse.AvailableDate> availableDateStream = schedules.stream().map(schedule -> new ConcertResponse.AvailableDate(schedule.scheduleId(), schedule.concertDate())).toList();
        return ResponseEntity.ok(availableDateStream);
    }

    @GetMapping(path = "/seats", produces = { "application/json" })
    @Operation(summary = "공연 좌석 조회", description = "예약 가능한 좌석을 조회합니다.", tags={ "concerts" })
    public ResponseEntity<List<ConcertResponse.AvailableSeat>> seat(Long concertScheduleId) {
        List<ConcertResult.SeatInfo> availableSeats = concertFacade.getAvailableSeats(concertScheduleId);
        List<ConcertResponse.AvailableSeat> availableSeatStream = availableSeats.stream().map(seat -> new ConcertResponse.AvailableSeat(seat.seatId(), seat.seatNumber(), seat.price())).toList();
        return ResponseEntity.ok(availableSeatStream);
    }
}
