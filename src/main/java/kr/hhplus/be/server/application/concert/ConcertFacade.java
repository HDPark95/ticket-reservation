package kr.hhplus.be.server.application.concert;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.concert.ConcertSchedule;
import kr.hhplus.be.server.domain.concert.ConcertService;
import kr.hhplus.be.server.domain.concert.Seat;
import kr.hhplus.be.server.domain.reservation.Reservation;
import kr.hhplus.be.server.domain.reservation.ReservationService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;
    private final ReservationService reservationService;
    private final UserService userService;

    @Transactional
    public List<ConcertResult.ScheduleInfo> getSchedules(Long concertId){
        List<ConcertSchedule> schedules = concertService.getSchedules(concertId);
        return schedules.stream().map(ConcertResult.ScheduleInfo::from).toList();
    }

    @Transactional
    public ConcertResult.ReservationResult reserve(ConcertCriteria.ReserveSeat command){
        User user = userService.getUser(command.userId());
        Seat seat = concertService.getSeat(command.seatId());
        Reservation reservation = reservationService.reserve(user, seat);
        return ConcertResult.ReservationResult.from(reservation);
    }

    @Transactional
    public List<ConcertResult.SeatInfo> getAvailableSeats(Long scheduleId){
        List<Reservation> reservations = reservationService.getValidReservationsByScheduleId(scheduleId);
        List<Long> reservedSeatIds = reservations.stream().map(reservation -> reservation.getSeat().getId()).toList();
        List<Seat> seats = concertService.getAvailableSeats(scheduleId, reservedSeatIds);
        return seats.stream().map(ConcertResult.SeatInfo::from).toList();
    }

}
