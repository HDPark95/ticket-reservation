package kr.hhplus.be.server.intrastructure.dataportal;

import org.springframework.stereotype.Component;

@Component
public class DataPortalAdapter {

    public DataPortalResponse sendReservationInfo(ReservationInfo reservationInfo) {
        return new DataPortalResponse("200", "Success");
    }
}
