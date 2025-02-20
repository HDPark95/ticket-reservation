package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Table(schema = "tb_payment_complete_outbox")
public class PaymentCompleteOutbox {

    @Id
    private Long id;

    @Column(name="message_id")
    private String messageId;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING, SUCCESS
    }

    @Column(name="send_at")
    private LocalDateTime sendAt;

    public static PaymentCompleteOutbox put(Long paymentId, Clock clock) {
        return PaymentCompleteOutbox.builder()
                .id(paymentId)
                .status(Status.PENDING)
                .sendAt(LocalDateTime.now(clock))
                .build();
    }

    @Builder
    public PaymentCompleteOutbox(Long id, Status status, LocalDateTime sendAt) {
        this.id = id;
        this.status = status;
        this.sendAt = sendAt;
    }
}
