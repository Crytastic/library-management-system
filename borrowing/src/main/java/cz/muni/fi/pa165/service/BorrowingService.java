package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.Borrowing;
import cz.muni.fi.pa165.data.repository.BorrowingRepository;
import cz.muni.fi.pa165.exceptionhandling.exceptions.ResourceNotFoundException;
import cz.muni.fi.pa165.exceptionhandling.exceptions.UnableToContactServiceException;
import cz.muni.fi.pa165.exceptionhandling.exceptions.UnauthorizedException;
import cz.muni.fi.pa165.util.ServiceHttpRequestProvider;
import cz.muni.fi.pa165.util.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for managing borrowing of books.
 * Provides methods to interact with borrowings.
 *
 * @author Maxmilián Šeffer
 */
@Service
public class BorrowingService {
    private final BorrowingRepository borrowingRepository;

    private final ServiceHttpRequestProvider serviceHttpRequestProvider;

    @Autowired
    public BorrowingService(BorrowingRepository borrowingRepository, ServiceHttpRequestProvider serviceHttpRequestProvider) {
        this.borrowingRepository = borrowingRepository;
        this.serviceHttpRequestProvider = serviceHttpRequestProvider;
    }

    @Transactional
    public List<Borrowing> findAll() {
        return borrowingRepository.findAll();
    }

    public Borrowing createBorrowing(Long bookId, Long borrowerId, OffsetDateTime expectedReturnDate, BigDecimal lateReturnWeeklyFine) {
        try {
            ResponseEntity<String> ignoredBook = serviceHttpRequestProvider.callGetBookById(bookId);
            ResponseEntity<String> ignoredUser = serviceHttpRequestProvider.callGetUserById(borrowerId);
            Borrowing borrowing = new Borrowing(bookId,
                    borrowerId,
                    TimeProvider.now(),
                    expectedReturnDate == null ? getDefaultExpectedReturnDate() : expectedReturnDate,
                    false,
                    null,
                    lateReturnWeeklyFine == null ? getDefaultLateReturnWeeklyFine() : lateReturnWeeklyFine,
                    false);
            return borrowingRepository.save(borrowing);
        } catch (RestClientException e) {
            String message = e.getMessage().strip();
            String httpMessage = message.replace('\"', ' ').strip();
            switch (message.substring(0, 3)) {
                case "404" -> throw new ResourceNotFoundException(httpMessage);
                case "401" -> throw new UnauthorizedException(httpMessage);
                default -> throw new UnableToContactServiceException(httpMessage);
            }
        }
    }

    @Transactional
    public Borrowing findById(Long id) {
        return borrowingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Borrowing with id: %d not found", id)));
    }

    @Transactional
    public void deleteById(Long id) {
        borrowingRepository.deleteById(id);
    }

    @Transactional
    public Borrowing updateById(Long id, Long bookId, Long borrowerId, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate, Boolean returned, OffsetDateTime returnDate, BigDecimal lateReturnWeeklyFine, Boolean fineResolved) {
        int updatedCount = borrowingRepository.updateById(id, bookId, borrowerId, borrowDate, expectedReturnDate, returned, returnDate, lateReturnWeeklyFine, fineResolved);
        if (updatedCount > 0) {
            return findById(id);
        } else {
            throw new ResourceNotFoundException(String.format("Borrowing with id: %d not found", id));
        }
    }

    @Transactional
    public BigDecimal getFineById(Long id) {
        // Throws ResourceNotFoundException when borrowing is not found
        Borrowing borrowing = findById(id);

        OffsetDateTime expectedReturnDate = borrowing.getExpectedReturnDate();

        // Book returned but fine not paid
        if (borrowing.isReturned()) {
            if (borrowing.isFineResolved()) {
                return BigDecimal.ZERO;
            }

            OffsetDateTime returnDate = borrowing.getReturnDate();
            return calculateFine(expectedReturnDate, returnDate, borrowing);
        }

        // Book not yet returned
        OffsetDateTime currentDate = TimeProvider.now();
        return calculateFine(expectedReturnDate, currentDate, borrowing);
    }

    @Transactional
    public void deleteAll() {
        borrowingRepository.deleteAll();
    }

    private BigDecimal calculateFine(OffsetDateTime expectedReturnDate, OffsetDateTime returnDate, Borrowing borrowing) {
        if (returnDate.isBefore(expectedReturnDate)) {
            return BigDecimal.ZERO;
        }

        Duration delayDuration = Duration.between(expectedReturnDate, returnDate);
        long weeksDelayed = (delayDuration.toDays() + 6) / 7;
        BigDecimal lateReturnWeeklyFine = borrowing.getLateReturnWeeklyFine();
        return lateReturnWeeklyFine.multiply(BigDecimal.valueOf(weeksDelayed));
    }

    private OffsetDateTime getDefaultExpectedReturnDate() {
        return TimeProvider.now().plusMonths(3);
    }

    private BigDecimal getDefaultLateReturnWeeklyFine() {
        return BigDecimal.ONE;
    }

    @Transactional
    public List<Borrowing> findAllActive() {
        return borrowingRepository.findAll().stream().filter(b -> !b.isReturned()).collect(Collectors.toList());
    }

}
