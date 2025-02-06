package kr.hhplus.be.server.domain.waitingtoken;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.domain.core.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WaitingToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "token")
    private String token;

    public static WaitingToken issue(Long userId) {
        return WaitingToken.builder()
                .userId(userId)
                .token(UUID.randomUUID().toString())
                .status(TokenStatus.WAITING)
                .build();
    }

    @Builder
    protected WaitingToken(Long id, Long userId, String token, TokenStatus status) {
        this.id = id;
        this.userId = userId;
        this.token = token;
    }

}

