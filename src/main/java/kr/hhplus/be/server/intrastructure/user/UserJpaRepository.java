package kr.hhplus.be.server.intrastructure.user;

import kr.hhplus.be.server.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {

}
