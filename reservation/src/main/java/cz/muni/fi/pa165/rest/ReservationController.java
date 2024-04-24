package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.ReservationFacade;
import org.openapitools.api.ReservationApi;
import org.openapitools.model.ReservationDTO;
import org.openapitools.model.ReservationTestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * REST controller layer for managing reservations.
 * Handles HTTP requests related to reservation management.
 *
 * @author Martin Suchánek
 */
@RestController
public class ReservationController implements ReservationApi {
    ReservationFacade reservationFacade;

    @Autowired
    public ReservationController(ReservationFacade reservationFacade) {
        this.reservationFacade = reservationFacade;
    }

    @Override
    public ResponseEntity<ReservationDTO> createReservation(Long bookId, Long reserveeId) {
        return new ResponseEntity<>(reservationFacade.createReservation(bookId, reserveeId), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteReservation(Long id) {
        reservationFacade.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<ReservationDTO>> getActiveReservations() {
        return new ResponseEntity<>(reservationFacade.findAllActive(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ReservationDTO>> getExpiredReservations() {
        return new ResponseEntity<>(reservationFacade.findAllExpired(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ReservationDTO> getReservation(Long id) {
        return new ResponseEntity<>(reservationFacade.findById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ReservationDTO>> getReservations() {
        return new ResponseEntity<>(reservationFacade.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ReservationTestResponse> test() {
        return new ResponseEntity<>(new ReservationTestResponse().message("Service running"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateReservation(Long id, Long bookId, Long reserveeId, OffsetDateTime reservedFrom, OffsetDateTime reservedTo) {
        reservationFacade.updateById(id, bookId, reserveeId, reservedFrom, reservedTo);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
