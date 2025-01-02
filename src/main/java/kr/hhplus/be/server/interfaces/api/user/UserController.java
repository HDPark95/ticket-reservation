package kr.hhplus.be.server.interfaces.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping(path="/{userId}/balance", produces = { "application/json" })
    @Operation(summary = "잔액 조회", description = "유저의 잔액을 조회합니다.", tags={ "users" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "잔액 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.Balance.class))),
            @ApiResponse(responseCode = "400", description = "요청 오류") })
    public ResponseEntity<UserResponse.Balance> getBalance(@PathVariable String userId) {
        return ResponseEntity.ok(new UserResponse.Balance(BigDecimal.TEN));
    }

    @PostMapping(path = "/{userId}/balance", produces = { "application/json" }, consumes = { "application/json" })
    @Operation(summary = "잔액 추가", description = "유저의 잔액을 추가합니다.", tags={ "users" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "잔액 추가 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.Balance.class))),
            @ApiResponse(responseCode = "400", description = "요청 오류") })
    public ResponseEntity<UserResponse.Balance> addBalance(@PathVariable String userId, @RequestBody UserRequest.Add request) {
        return ResponseEntity.ok(new UserResponse.Balance(BigDecimal.valueOf(100L)));
    }
}
