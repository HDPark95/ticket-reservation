package kr.hhplus.be.server.interfaces.api.payment;

import io.swagger.v3.oas.annotations.Operation;;
import jakarta.validation.Valid;
import kr.hhplus.be.server.domain.payment.PaymentResult;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.interfaces.handler.TokenUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "결제", description = "결제를 진행합니다.", tags={ "payments" })
    public ResponseEntity<PaymentResponse> pay(@RequestBody @Valid PaymentRequest.pay request, @TokenUserId Long userId) {
        PaymentResult pay = paymentService.pay(request.reservationId(), userId);
        return ResponseEntity.ok(PaymentResponse.from(pay));
    }
}
