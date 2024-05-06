package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.BorrowingFacade;
import org.openapitools.api.BorrowingApi;
import org.openapitools.model.BorrowingDTO;
import org.openapitools.model.BorrowingTestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * REST controller layer for managing borrowings.
 * Handles HTTP requests related to borrowings management.
 *
 * @author Maxmilián Šeffer
 */
@Controller
public class BorrowingController implements BorrowingApi {
    private final BorrowingFacade borrowingFacade;

    @Autowired
    public BorrowingController(BorrowingFacade borrowingFacade) {
        this.borrowingFacade = borrowingFacade;
    }


    @Override
    public ResponseEntity<BorrowingTestResponse> test() {
        return new ResponseEntity<>(new BorrowingTestResponse().message("Service running"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<BorrowingDTO>> getBorrowings() {
        return new ResponseEntity<>(borrowingFacade.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BorrowingDTO> createBorrowing(Long bookId, Long borrowerId, OffsetDateTime expectedReturnDate, BigDecimal lateReturnWeeklyFine) {
        return new ResponseEntity<>(borrowingFacade.createBorrowing(bookId, borrowerId, expectedReturnDate, lateReturnWeeklyFine), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<BorrowingDTO> getBorrowing(Long id) {
        return new ResponseEntity<>(borrowingFacade.findById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateBorrowing(Long id, Long bookId, Long borrowerId, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate, Boolean returned, OffsetDateTime returnDate, BigDecimal lateReturnWeeklyFine, Boolean fineResolved) {
        borrowingFacade.updateById(id, bookId, borrowerId, borrowDate, expectedReturnDate, returned, returnDate, lateReturnWeeklyFine, fineResolved);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteBorrowing(Long id) {
        borrowingFacade.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BigDecimal> getFineById(Long id) {
        return new ResponseEntity<>(borrowingFacade.getFineById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> deleteBorrowings() {
        borrowingFacade.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
