package kr.hhplus.be.server.domain.concert;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.core.BaseEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "tb_concert_schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcertSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), nullable = false)
    private Concert concert;

    @Column(name = "date")
    private LocalDate date;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "concert_schedule_id")
    @Builder.Default
    private List<Seat> seats = new ArrayList<>();

    public void addSeat(Integer seatNumber, BigDecimal price) {
        this.seats.add(Seat.builder()
                .seatNumber(seatNumber)
                .price(price)
                .concertSchedule(this)
                .build());
    }
}
