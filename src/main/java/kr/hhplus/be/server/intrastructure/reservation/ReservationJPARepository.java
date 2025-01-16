package kr.hhplus.be.server.intrastructure.reservation;

import kr.hhplus.be.server.domain.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationJPARepository extends JpaRepository<Reservation, Long> {
}
