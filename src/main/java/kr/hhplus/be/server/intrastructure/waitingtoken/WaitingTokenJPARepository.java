package kr.hhplus.be.server.intrastructure.waitingtoken;

import kr.hhplus.be.server.domain.waitingtoken.WaitingToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface WaitingTokenJPARepository extends JpaRepository<WaitingToken, Long> {

    @Modifying
    @Query(
            value = """ 
                UPDATE tb_waiting_token wt
                SET status = 'ACTIVATE' 
                WHERE id IN (
                    SELECT subquery.id 
                    FROM (
                        SELECT wt2.id 
                        FROM tb_waiting_token wt2 
                        ORDER BY wt2.id ASC 
                        LIMIT 100
                    ) subquery
                )
            """,
            nativeQuery = true
    )
    void activateTop100Tokens();


    @Query("""
            SELECT COUNT(wt) + 1 
            FROM WaitingToken wt 
            WHERE wt.expiredAt > :now 
            AND wt.status = 'WAITING' 
            AND wt.id < (SELECT w.id FROM WaitingToken w WHERE w.user.id = :userId)
        """)
    Long findPositionByUserId(@Param("userId") Long userId, LocalDateTime now);

}
