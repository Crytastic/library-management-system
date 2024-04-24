package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.Borrowing;
import cz.muni.fi.pa165.data.repository.BorrowingRepository;
import cz.muni.fi.pa165.exceptionhandling.exceptions.ResourceNotFoundException;
import cz.muni.fi.pa165.util.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing borrowing of books.
 * Provides methods to interact with borrowings.
 *
 * @author Maxmilián Šeffer
 */
@Service
public class BorrowingService {
    private final BorrowingRepository borrowingRepository;

    @Autowired
    public BorrowingService(BorrowingRepository borrowingRepository) {
        this.borrowingRepository = borrowingRepository;
    }

    @Transactional
    public List<Borrowing> findAll() {
        return borrowingRepository.findAll();
    }

    public Borrowing createBorrowing(Long bookId, Long borrowerId, OffsetDateTime expectedReturnDate, BigDecimal lateReturnWeeklyFine) {
        Borrowing borrowing = new Borrowing(bookId,
                borrowerId,
                TimeProvider.now(),
                expectedReturnDate == null ? getDefaultExpectedReturnDate() : expectedReturnDate,
                false,
                null,
                lateReturnWeeklyFine == null ? getDefaultLateReturnWeeklyFine() : lateReturnWeeklyFine,
                false);
        return borrowingRepository.save(borrowing);
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
    public int updateById(Long id, Long bookId, Long borrowerId, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate, Boolean returned, OffsetDateTime returnDate, BigDecimal lateReturnWeeklyFine, Boolean fineResolved) {
        return borrowingRepository.updateById(id, bookId, borrowerId, borrowDate, expectedReturnDate, returned, returnDate, lateReturnWeeklyFine, fineResolved);
    }

    @Transactional
    public Optional<BigDecimal> getFineById(Long id) {
        // Throws ResourceNotFoundException when borrowing is not found
        Borrowing borrowing = findById(id);

        OffsetDateTime expectedReturnDate = borrowing.getExpectedReturnDate();

        // Book returned but fine not paid
        if (borrowing.isReturned()) {
            if (borrowing.isFineResolved()) {
                return Optional.of(BigDecimal.ZERO);
            }

            OffsetDateTime returnDate = borrowing.getReturnDate();
            BigDecimal fine = calculateFine(expectedReturnDate, returnDate, borrowing);
            return Optional.of(fine);
        }

        // Book not yet returned
        OffsetDateTime currentDate = TimeProvider.now();
        BigDecimal fine = calculateFine(expectedReturnDate, currentDate, borrowing);
        return Optional.of(fine);
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
}
