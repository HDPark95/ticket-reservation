package kr.hhplus.be.server.domain.reservation;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.concert.Seat;
import kr.hhplus.be.server.domain.core.BaseEntity;
import kr.hhplus.be.server.domain.user.User;
import lombok.*;

import java.time.Clock;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "tb_reservation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), nullable = false)
    private Long userId;

    @JoinColumn(name = "seat_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), nullable = false)
    private Long seatId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(255)", nullable = false)
    private ReservationStatus status;

    @Column(name = "expired_at", columnDefinition = "datetime", nullable = false)
    private LocalDateTime expiredAt;

    public static Reservation createPending(Long userId, Long seatId, LocalDateTime now){
        return Reservation.builder()
                .userId(userId)
                .seatId(seatId)
                .status(ReservationStatus.PENDING)
                .expiredAt(now.plusMinutes(30))
                .build();
    }

    public void reserved(LocalDateTime now){
        if (this.status == ReservationStatus.RESERVED){
            throw new AlreadyPaidReservationException("이미 결제된 예약입니다.");
        }
        if (this.expiredAt.isBefore(now)){
            throw new ReservationExpiredException("예약 좌석 결제 시간이 만료되었습니다.");
        }
        this.status = ReservationStatus.RESERVED;
    }

    public void rollbackStatusToPending() {
        this.status = ReservationStatus.PENDING;
    }
}
