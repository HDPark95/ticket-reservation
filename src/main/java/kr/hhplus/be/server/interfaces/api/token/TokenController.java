package kr.hhplus.be.server.interfaces.api.token;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.hhplus.be.server.application.waitingtoken.WaitingTokenFacade;
import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/token")
public class TokenController {

    private final WaitingTokenFacade waitingTokenFacade;

    @PostMapping(produces = { "application/json" }, consumes = { "application/json" })
    @Operation(summary = "토큰 발급", description = "유저를 대기열에 등록하고 토큰을 발급합니다.", tags={ "token" })
    public ResponseEntity<TokenResponse> issueToken(@RequestBody @Valid TokenRequest request) {
        WaitingTokenResult issue = waitingTokenFacade.issue(request.phoneNumber());
        return ResponseEntity.ok(TokenResponse.from(issue));
    }

    @GetMapping(produces = { "application/json" })
    @Operation(summary = "토큰 대기열 정보 조회", description = "토큰 대기열 정보 조회", tags={ "token" })
    public ResponseEntity<TokenResponse> getWaitingToken(HttpServletRequest request) {
        String token = request.getHeader("X-Waiting-Token");
        return ResponseEntity.ok(TokenResponse.from(waitingTokenFacade.getWaitingToken(token)));
    }
}
