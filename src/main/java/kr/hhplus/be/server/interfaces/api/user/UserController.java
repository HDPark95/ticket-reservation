package kr.hhplus.be.server.interfaces.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import kr.hhplus.be.server.application.userpoint.UserPointFacade;
import kr.hhplus.be.server.application.userpoint.UserPointResult;
import kr.hhplus.be.server.interfaces.handler.TokenUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserPointFacade userPointFacade;

    @GetMapping(path="/balance", produces = { "application/json" })
    @Operation(summary = "잔액 조회", description = "유저의 잔액을 조회합니다.", tags={ "users" })
    public ResponseEntity<UserResponse.Balance> getBalance(@TokenUserId Long userId) {
        UserPointResult pointInfo = userPointFacade.getPointInfo(userId);
        return ResponseEntity.ok(new UserResponse.Balance(pointInfo.point()));
    }

    @PostMapping(path = "/balance", produces = { "application/json" }, consumes = { "application/json" })
    @Operation(summary = "잔액 추가", description = "유저의 잔액을 추가합니다.", tags={ "users" })
    public ResponseEntity<UserResponse.Balance> addBalance(@TokenUserId Long userId, @RequestBody @Valid UserRequest.Add request) {
        UserPointResult userPointResult = userPointFacade.addPoint(userId, request.amount());
        return ResponseEntity.ok(new UserResponse.Balance(userPointResult.point()));
    }
}
