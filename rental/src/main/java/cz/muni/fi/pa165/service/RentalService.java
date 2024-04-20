package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.RentalDAO;
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

    private final RentalRepository rentalRepository;

    @Autowired
    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public List<RentalDAO> findAll() {
        return rentalRepository.findAll();
    }

    public RentalDAO createRental(String book, String rentedBy, OffsetDateTime expectedReturnDate, BigDecimal lateReturnWeeklyFine) {
        RentalDAO rentalDAO = new RentalDAO(book,
                rentedBy,
                TimeProvider.now(),
                expectedReturnDate == null ? getDefaultExpectedReturnDate() : expectedReturnDate,
                false,
                null,
                lateReturnWeeklyFine == null ? getDefaultLateReturnWeeklyFine() : lateReturnWeeklyFine,
                false);
        return rentalRepository.store(rentalDAO);
    }

    public Optional<RentalDAO> findById(Long id) {
        return rentalRepository.findById(id);
    }

    public boolean deleteById(Long id) {
        return rentalRepository.deleteById(id);
    }

    public Optional<RentalDAO> updateById(Long id, String book, String rentedBy, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate, Boolean returned, OffsetDateTime returnDate, BigDecimal lateReturnWeeklyFine, Boolean fineResolved) {
        Optional<RentalDAO> optionalRental = rentalRepository.findById(id);

        if (optionalRental.isEmpty()) {
            return Optional.empty();
        }

        RentalDAO rentalDAO = optionalRental.get();

        rentalDAO.setBook(book != null ? book : rentalDAO.getBook());
        rentalDAO.setRentedBy(rentedBy != null ? rentedBy : rentalDAO.getRentedBy());
        rentalDAO.setBorrowDate(borrowDate != null ? borrowDate : rentalDAO.getBorrowDate());
        rentalDAO.setExpectedReturnDate(expectedReturnDate != null ? expectedReturnDate : rentalDAO.getExpectedReturnDate());
        rentalDAO.setReturned(returned != null ? returned : rentalDAO.isReturned());
        rentalDAO.setReturnDate(returnDate != null ? returnDate : rentalDAO.getReturnDate());
        rentalDAO.setLateReturnWeeklyFine(lateReturnWeeklyFine != null ? lateReturnWeeklyFine : rentalDAO.getLateReturnWeeklyFine());
        rentalDAO.setFineResolved(fineResolved != null ? fineResolved : rentalDAO.isFineResolved());

        return rentalRepository.updateById(rentalDAO.getId(), rentalDAO);
    }

    public Optional<BigDecimal> getFineById(Long id) {
        Optional<RentalDAO> optionalRental = findById(id);

        // Rental not found
        if (optionalRental.isEmpty()) {
            return Optional.empty();
        }

        RentalDAO rental = optionalRental.get();
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

    private BigDecimal calculateFine(OffsetDateTime expectedReturnDate, OffsetDateTime returnDate, RentalDAO rental) {
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
