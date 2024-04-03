package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.ReservationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    ReservationFacade reservationFacade;


    @Autowired
    public ReservationController(ReservationFacade reservationFacade) {
        this.reservationFacade = reservationFacade;
    }
}
