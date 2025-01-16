package kr.hhplus.be.server.intrastructure.concert;

import kr.hhplus.be.server.domain.concert.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatJPARepository extends JpaRepository<Seat, Long> {
}
