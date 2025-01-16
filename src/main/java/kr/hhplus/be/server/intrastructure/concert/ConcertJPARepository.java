package kr.hhplus.be.server.intrastructure.concert;

import kr.hhplus.be.server.domain.concert.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertJPARepository extends JpaRepository<Concert, Long> {
}
