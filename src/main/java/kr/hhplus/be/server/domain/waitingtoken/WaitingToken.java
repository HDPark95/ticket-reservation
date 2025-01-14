package kr.hhplus.be.server.domain.waitingtoken;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.core.BaseEntity;
import kr.hhplus.be.server.domain.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Table(name = "tb_waiting_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), nullable = false, unique = true)
    private User user;

    @Column(name = "token")
    private String token;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TokenStatus status;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    public static WaitingToken issue(Long userId, Clock clock) {
        LocalDateTime now = LocalDateTime.now(clock);
        return WaitingToken.builder()
                .user(User.builder().id(userId).build())
                .token(UUID.randomUUID().toString())
                .status(TokenStatus.WAITING)
                .expiredAt(now.plusMinutes(30))
                .build();
    }

    @Builder
    protected WaitingToken(Long id, User user, String token, TokenStatus status, LocalDateTime expiredAt) {
        this.id = id;
        this.user = user;
        this.token = token;
        this.status = status;
        this.expiredAt = expiredAt;
    }

    public void active() {
        this.status = TokenStatus.ACTIVATE;
    }

    public boolean validate(Clock clock) {
        LocalDateTime now = LocalDateTime.now(clock);
        return this.status == TokenStatus.ACTIVATE && this.expiredAt.isAfter(now);
    }
}

