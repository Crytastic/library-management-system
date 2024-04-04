package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.RentalFacade;
import org.openapitools.api.RentalApi;
import org.openapitools.model.Rental;
import org.openapitools.model.RentalTestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<List<Rental>> getRentals() {
        return new ResponseEntity<>(rentalFacade.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Rental> createRental(String book, String rentedBy) {
        Rental createdRental = rentalFacade.createRental(book, rentedBy);
        return new ResponseEntity<>(createdRental, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Rental> getRental(Long id) {
        Optional<Rental> rental = rentalFacade.findById(id);
        return rental.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Rental> updateRental(Long id, String book, String rentedBy, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate, Boolean returned, OffsetDateTime returnDate, BigDecimal lateReturnWeeklyFine, Boolean fineResolved) {
        Optional<Rental> rental = rentalFacade.updateById(id, book, rentedBy, borrowDate, expectedReturnDate, returned, returnDate, lateReturnWeeklyFine, fineResolved);
        return rental.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<Void> deleteRental(Long id) {
        boolean deleted = rentalFacade.deleteById(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<BigDecimal> getFineById(Long id) {
        Optional<BigDecimal> fine = rentalFacade.getFineById(id);
        return fine.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
