package kr.hhplus.be.server.intrastructure.concert;

import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertSchduleJPARepository extends JpaRepository<ConcertSchedule, Long> {
}
