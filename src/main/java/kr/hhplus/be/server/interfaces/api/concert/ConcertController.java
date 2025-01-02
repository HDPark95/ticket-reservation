package kr.hhplus.be.server.interfaces.api.concert;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.interfaces.api.user.UserResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/concerts")
public class ConcertController {

    @GetMapping(path="/dates", produces = { "application/json" })
    @Operation(summary = "공연 일자 조회", description = "예약 가능한 일자를 조회합니다.", tags={ "concerts" })
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "일자 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConcertResponse.AvailableDate.class))),
                    @ApiResponse(responseCode = "400", description = "요청 오류")
            }
    )
    public ResponseEntity<List<ConcertResponse.AvailableDate>> date() {
        return ResponseEntity.ok(List.of(new ConcertResponse.AvailableDate(
                LocalDate.of(2025, 1, 1)
        )));
    }

    @GetMapping(path = "/seats", produces = { "application/json" })
    @Operation(summary = "공연 좌석 조회", description = "예약 가능한 좌석을 조회합니다.", tags={ "concerts" })
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "좌석 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ConcertResponse.AvailableSeat.class))),
                    @ApiResponse(responseCode = "400", description = "요청 오류")
            }
    )
    public ResponseEntity<List<ConcertResponse.AvailableSeat>> seat(@Valid
                                                                        @RequestParam
                                                                        @NotNull(message = "날짜는 필수입니다.")
                                                                        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return ResponseEntity.ok(List.of(new ConcertResponse.AvailableSeat(
                1L,
                1,
                BigDecimal.valueOf(50000)
        )));
    }
}
