package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.ReservationFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.openapitools.api.ReservationApi;
import org.openapitools.model.ReservationDTO;
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
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ReservationDTO> createReservation(Long bookId, Long reserveeId) {
        return new ResponseEntity<>(reservationFacade.createReservation(bookId, reserveeId), HttpStatus.CREATED);
    }

    @Override
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteReservation(Long id) {
        reservationFacade.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<ReservationDTO>> getActiveReservations() {
        return new ResponseEntity<>(reservationFacade.findAllActive(), HttpStatus.OK);
    }

    @Override
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))

    public ResponseEntity<List<ReservationDTO>> getExpiredReservations() {
        return new ResponseEntity<>(reservationFacade.findAllExpired(), HttpStatus.OK);
    }

    @Override
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ReservationDTO> getReservation(Long id) {
        return new ResponseEntity<>(reservationFacade.findById(id), HttpStatus.OK);
    }

    @Override
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<ReservationDTO>> getReservations() {
        return new ResponseEntity<>(reservationFacade.findAll(), HttpStatus.OK);
    }

    @Override
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<ReservationDTO> updateReservation(Long id, Long bookId, Long reserveeId, OffsetDateTime reservedFrom, OffsetDateTime reservedTo) {
        return new ResponseEntity<>(reservationFacade.updateById(id, bookId, reserveeId, reservedFrom, reservedTo), HttpStatus.OK);
    }

    @Override
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteReservations() {
        reservationFacade.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
