package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationFacade {

    ReservationService reservationService;
    @Autowired
    public ReservationFacade(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
}
