package kr.hhplus.be.server.interfaces.api.token;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/token")
public class TokenController {

    @PostMapping(produces = { "application/json" }, consumes = { "application/json" })
    @Operation(summary = "토큰 발급", description = "유저를 대기열에 등록하고 토큰을 발급합니다.", tags={ "token" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "토큰 발급 성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "요청 오류") })
    @RequestMapping(value = "/api/v1/token",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    public ResponseEntity<TokenResponse> issueToken(@RequestBody TokenRequest request) {
        return ResponseEntity.ok(new TokenResponse("token", "success", 1L));
    }
}
