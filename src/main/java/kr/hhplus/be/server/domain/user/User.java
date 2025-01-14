package kr.hhplus.be.server.domain.user;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.core.BaseEntity;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "tb_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "point")
    private BigDecimal point;

    public void usePoint(BigDecimal amount) {
        if (this.point.compareTo(amount) < 0) {
            throw new InsufficientPointsException("포인트가 부족합니다.");
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativePointException("사용하려는 포인트는 0보다 작을 수 없습니다.");
        }

        this.point = this.point.subtract(amount);
    }

    public void addPoint(BigDecimal point) {
        if (point.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativePointException("적립하려는 포인트는 0보다 작을 수 없습니다.");
        }
        this.point = this.point.add(point);
    }
}
