package kr.hhplus.be.server.domain.concert;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ConcertServiceTest {

    @Autowired
    private ConcertService concertService;

    @Autowired
    private ConcertRepository concertRepository;

    @MockitoBean
    private Clock clock;

    @Test
    @Transactional
    @DisplayName("콘서트 아이디로 콘서트 날짜 조회")
    public void getConcertDateByConcertId() {
        // given
        Instant fixedInstant = Instant.parse("2025-01-01T10:00:00Z");
        ZoneId zoneId = ZoneId.of("UTC");

        when(clock.instant()).thenReturn(fixedInstant);
        when(clock.getZone()).thenReturn(zoneId);

        Concert concert = Concert
                .builder()
                .name("아이유 콘서트")
                .build();

        LocalDate startDate = LocalDate.now(clock);
        for (int i = 0; i < 50; i++) {
            concert.addConcertSchedule(startDate.plusDays(i));
        }
        concert = concertRepository.save(concert);

        // when
        List<ConcertSchedule> concertSchedules = concertService.getSchedules(concert.getId());

        // then
        assertThat(concertSchedules.size()).isEqualTo(50);
    }
}
