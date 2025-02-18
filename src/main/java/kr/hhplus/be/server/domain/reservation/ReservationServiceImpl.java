package kr.hhplus.be.server.domain.reservation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{

    private final ReservationRepository reservationRepository;
    private final Clock clock;

    @Override
    @Transactional
    public List<Reservation> getValidReservationsByScheduleId(Long scheduleId) {
        return reservationRepository.getValidReservationsByScheduleId(scheduleId, LocalDateTime.now(clock));
    }

    @Override
    @Transactional
    public Reservation reserve(Long userId, Long seatId) {
        reservationRepository.findAlreadySeatReservation(seatId)
                .ifPresent(reservation -> {
                    throw new ReservationAlreadyExistsException("이미 예약된 좌석입니다.");
                });
        return reservationRepository.save(Reservation.createPending(userId, seatId, LocalDateTime.now(clock)));
    }

    @Override
    @Transactional
    public Reservation getReservation(Long reservationId, Long userId) {
        return reservationRepository.findByIdAndUserIdForUpdate(reservationId, userId)
                .orElseThrow(() -> new ReservationNotFoundException("예약 정보가 없습니다."));
    }

    @Override
    @Transactional
    public void complete(Reservation reservation) {
        reservation.reserved(LocalDateTime.now(clock));
    }

    @Override
    @Transactional
    public void rollbackReservationComplete(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("예약 정보가 없습니다."));

        reservation.rollbackStatusToPending();
        //롤백한 예약은 다시 실패한 예약으로 처리
        reservationRepository.addFailCompleteReservation(reservationId, Instant.now(clock).toEpochMilli());
    }

    @Override
    public List<Long> getFailCompleteReservationIds() {
        return reservationRepository.getFailCompleteReservationIds();
    }

    @Override
    public void addFailCompleteReservation(Long reservationId) {
        reservationRepository.addFailCompleteReservation(reservationId, Instant.now(clock).toEpochMilli());
    }
}
