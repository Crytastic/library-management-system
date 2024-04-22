package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.RentalFacade;
import org.openapitools.api.RentalApi;
import org.openapitools.model.RentalDTO;
import org.openapitools.model.RentalTestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * REST controller layer for managing rentals.
 * Handles HTTP requests related to rentals management.
 *
 * @author Maxmilián Šeffer
 */
@Controller
public class RentalController implements RentalApi {

    private final RentalFacade rentalFacade;

    @Autowired
    public RentalController(RentalFacade rentalFacade) {
        this.rentalFacade = rentalFacade;
    }


    @Override
    public ResponseEntity<RentalTestResponse> test() {
        return new ResponseEntity<>(new RentalTestResponse().message("Service running"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<RentalDTO>> getRentals() {
        return new ResponseEntity<>(rentalFacade.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RentalDTO> createRental(Long bookId, Long borrowerId, OffsetDateTime expectedReturnDate, BigDecimal lateReturnWeeklyFine) {
        RentalDTO createdRental = rentalFacade.createRental(bookId, borrowerId, expectedReturnDate, lateReturnWeeklyFine);
        return new ResponseEntity<>(createdRental, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<RentalDTO> getRental(Long id) {
        Optional<RentalDTO> rental = rentalFacade.findById(id);
        return rental.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Void> updateRental(Long id, Long bookId, Long borrowerId, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate, Boolean returned, OffsetDateTime returnDate, BigDecimal lateReturnWeeklyFine, Boolean fineResolved) {
        int numberOfUpdatedRentals = rentalFacade.updateById(id, bookId, borrowerId, borrowDate, expectedReturnDate, returned, returnDate, lateReturnWeeklyFine, fineResolved);
        return new ResponseEntity<>(numberOfUpdatedRentals == 1 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<Void> deleteRental(Long id) {
        rentalFacade.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BigDecimal> getFineById(Long id) {
        Optional<BigDecimal> fine = rentalFacade.getFineById(id);
        return fine.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
