package kr.hhplus.be.server.interfaces.advice;

import kr.hhplus.be.server.domain.core.NotFoundException;
import kr.hhplus.be.server.domain.reservation.AlreadyPaidReservationException;
import kr.hhplus.be.server.domain.reservation.ReservationExpiredException;
import kr.hhplus.be.server.domain.user.InsufficientPointsException;
import kr.hhplus.be.server.domain.user.NegativePointException;
import kr.hhplus.be.server.domain.waitingtoken.WaitingTokenNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorAdvice {

    @ExceptionHandler(NegativePointException.class)
    public ResponseEntity<ErrorResponse> handleNegativePointException(NegativePointException e) {
        log.error("잘못된 포인트 충전을 시도하였습니다.", e);
        return ResponseEntity
                .status(400)
                .body(new ErrorResponse("잘못된 포인트 충전을 시도하였습니다."));
    }
    @ExceptionHandler(InsufficientPointsException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientPointsException(InsufficientPointsException e) {
        log.error("잔액이 부족합니다.", e);
        return ResponseEntity
                .status(400)
                .body(new ErrorResponse("잔액이 부족합니다."));
    }
    @ExceptionHandler(ReservationExpiredException.class)
    public ResponseEntity<ErrorResponse> handleReservationExpiredException(ReservationExpiredException e) {
        log.error("결제 기한이 지난 예약입니다.", e);
        return ResponseEntity
                .status(400)
                .body(new ErrorResponse("결제 기한이 지난 예약입니다."));
    }
    @ExceptionHandler(WaitingTokenNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleWaitingTokenNotFoundException(WaitingTokenNotFoundException e) {
        log.error("유효 하지 않은 대기열 토큰 입니다. ", e);
        return ResponseEntity
                .status(404)
                .body(new ErrorResponse("유효 하지 않은 대기열 토큰 입니다."));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        log.error("요청한 리소스를 찾을 수 없습니다: {}", e);
        return ResponseEntity
                .status(404)
                .body(new ErrorResponse("요청한 리소스를 찾을 수 없습니다."));
    }

    @ExceptionHandler(AlreadyPaidReservationException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyPaidReservationException(AlreadyPaidReservationException e) {
        log.error("이미 결제된 예약입니다.", e);
        return ResponseEntity
                .status(400)
                .body(new ErrorResponse("이미 결제된 예약입니다."));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("서버 에러가 발생했습니다.", e);
        return ResponseEntity
                .status(500)
                .body(new ErrorResponse("서버 에러가 발생했습니다."));
    }
}
