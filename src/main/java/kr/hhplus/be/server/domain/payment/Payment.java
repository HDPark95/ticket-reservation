package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.core.BaseEntity;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.user.User;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Entity
@Table(name = "tb_payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), nullable = false)
    private Reservation reservation;

    @Column(name = "concert_date", nullable = false)
    private LocalDate concertDate;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Column(name = "seat_price", nullable = false)
    private BigDecimal seatPrice;

    public static Payment create(Long userId, Long reservationId, LocalDate concertDate, Integer seatNumber, BigDecimal seatPrice) {
        return Payment.builder()
                .user(User.builder().id(userId).build())
                .reservation(Reservation.builder().id(reservationId).build())
                .concertDate(concertDate)
                .seatNumber(seatNumber)
                .seatPrice(seatPrice)
                .build();
    }
}
