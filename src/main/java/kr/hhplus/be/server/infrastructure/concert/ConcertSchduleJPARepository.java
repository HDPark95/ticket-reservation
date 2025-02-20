package kr.hhplus.be.server.infrastructure.concert;

import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertSchduleJPARepository extends JpaRepository<ConcertSchedule, Long> {
}
