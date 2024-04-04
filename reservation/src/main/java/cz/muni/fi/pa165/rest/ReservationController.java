package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.ReservationFacade;
import org.openapitools.api.ReservationApi;
import org.openapitools.model.ReservationDTO;
import org.openapitools.model.ReservationTestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReservationController implements ReservationApi {

    ReservationFacade reservationFacade;


    @Autowired
    public ReservationController(ReservationFacade reservationFacade) {
        this.reservationFacade = reservationFacade;
    }

    @Override
    public ResponseEntity<List<ReservationDTO>> getReservations() {
        return new ResponseEntity<>(reservationFacade.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ReservationTestResponse> test() {
        return new ResponseEntity<>(new ReservationTestResponse().message("Service running"), HttpStatus.OK);
    }
}
