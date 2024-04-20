package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.Rental;
import cz.muni.fi.pa165.data.repository.JpaRentalRepository;
import cz.muni.fi.pa165.repository.RentalRepository;
import cz.muni.fi.pa165.util.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing rental.
 * Provides methods to interact with rental.
 *
 * @author Maxmilián Šeffer
 */
@Service
public class RentalService {

    private final JpaRentalRepository jpaRentalRepository;

    @Autowired
    public RentalService(JpaRentalRepository jpaBookRepository, RentalRepository rentalRepository) {
        this.jpaRentalRepository = jpaBookRepository;
    }

    public List<Rental> findAll() {
        return jpaRentalRepository.findAll();
    }

    public Rental createRental(String book, String rentedBy, OffsetDateTime expectedReturnDate, BigDecimal lateReturnWeeklyFine) {
        Rental rental = new Rental(book,
                rentedBy,
                TimeProvider.now(),
                expectedReturnDate == null ? getDefaultExpectedReturnDate() : expectedReturnDate,
                false,
                null,
                lateReturnWeeklyFine == null ? getDefaultLateReturnWeeklyFine() : lateReturnWeeklyFine,
                false);
        return jpaRentalRepository.save(rental);
    }

    public Optional<Rental> findById(Long id) {
        return jpaRentalRepository.findById(id);
    }

    public void deleteById(Long id) {
        jpaRentalRepository.deleteById(id);
    }

    public int updateById(Long id, String book, String rentedBy, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate, Boolean returned, OffsetDateTime returnDate, BigDecimal lateReturnWeeklyFine, Boolean fineResolved) {
        return jpaRentalRepository.updateById(id, book, rentedBy, borrowDate, expectedReturnDate, returned, returnDate, lateReturnWeeklyFine, fineResolved);
    }

    public Optional<BigDecimal> getFineById(Long id) {
        Optional<Rental> optionalRental = findById(id);

        // Rental not found
        if (optionalRental.isEmpty()) {
            return Optional.empty();
        }

        Rental rental = optionalRental.get();
        OffsetDateTime expectedReturnDate = rental.getExpectedReturnDate();

        // Book returned but fine not paid
        if (rental.isReturned()) {
            if (rental.isFineResolved()) {
                return Optional.of(BigDecimal.ZERO);
            }

            OffsetDateTime returnDate = rental.getReturnDate();
            BigDecimal fine = calculateFine(expectedReturnDate, returnDate, rental);
            return Optional.of(fine);
        }

        // Book not yet returned
        OffsetDateTime currentDate = TimeProvider.now();
        BigDecimal fine = calculateFine(expectedReturnDate, currentDate, rental);
        return Optional.of(fine);
    }

    private BigDecimal calculateFine(OffsetDateTime expectedReturnDate, OffsetDateTime returnDate, Rental rental) {
        if (returnDate.isBefore(expectedReturnDate)) {
            return BigDecimal.ZERO;
        }

        Duration delayDuration = Duration.between(expectedReturnDate, returnDate);
        long weeksDelayed = (delayDuration.toDays() + 6) / 7;
        BigDecimal lateReturnWeeklyFine = rental.getLateReturnWeeklyFine();
        return lateReturnWeeklyFine.multiply(BigDecimal.valueOf(weeksDelayed));
    }

    private OffsetDateTime getDefaultExpectedReturnDate() {
        return TimeProvider.now().plusMonths(3);
    }

    private BigDecimal getDefaultLateReturnWeeklyFine() {
        return BigDecimal.ONE;
    }
}
