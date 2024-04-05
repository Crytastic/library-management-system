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
import java.util.Optional;

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
    public ResponseEntity<ReservationDTO> createReservation(String book, String reservedBy) {
        ReservationDTO createdReservation = reservationFacade.createReservation(book, reservedBy);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
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
        Optional<ReservationDTO> reservation = reservationFacade.findById(id);
        return reservation.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
    public ResponseEntity<ReservationDTO> updateReservation(Long id, String book, String reservedBy, OffsetDateTime reservedFrom, OffsetDateTime reservedTo) {
        Optional<ReservationDTO> reservation = reservationFacade.updateById(id, book, reservedBy, reservedFrom, reservedTo);
        return reservation.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
