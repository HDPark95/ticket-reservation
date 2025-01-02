package kr.hhplus.be.server.interfaces.api.payment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @PostMapping
    @Operation(summary = "결제", description = "결제를 진행합니다.", tags={ "payments" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 성공"),
            @ApiResponse(responseCode = "402", description = "잔액이 부족 합니다."),
            @ApiResponse(responseCode = "404", description = "잘못된 예약 아이디 입니다.")})
    public ResponseEntity<String> pay(PaymentRequest.pay request) {
        return ResponseEntity.ok("결제가 완료되었습니다.");
    }
}
