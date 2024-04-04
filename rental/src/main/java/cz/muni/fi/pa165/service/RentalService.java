package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.dao.RentalDAO;
import cz.muni.fi.pa165.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

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

    public RentalDAO createRental(String book, String rentedBy, OffsetDateTime borrowDate) {
        return createRental(book, rentedBy, borrowDate, getDefaultExpectedReturnDate(), getDefaultLateReturnWeeklyFine());
    }

    public RentalDAO createRental(String book, String rentedBy, OffsetDateTime borrowDate, BigDecimal lateReturnWeeklyFine) {
        return createRental(book, rentedBy, borrowDate, getDefaultExpectedReturnDate(), lateReturnWeeklyFine);
    }

    public RentalDAO createRental(String book, String rentedBy, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate) {
        return createRental(book, rentedBy, borrowDate, expectedReturnDate, getDefaultLateReturnWeeklyFine());
    }

    public RentalDAO createRental(String book, String rentedBy, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate, BigDecimal lateReturnWeeklyFine) {
        RentalDAO rentalDAO = new RentalDAO(book, rentedBy, borrowDate, expectedReturnDate, false, null, lateReturnWeeklyFine);
        return rentalRepository.store(rentalDAO);
    }

    public Optional<RentalDAO> findById(Long id) {
        return rentalRepository.findById(id);
    }

    public boolean deleteById(Long id) {
        return rentalRepository.deleteById(id);
    }

    public Optional<RentalDAO> updateById(Long id, String book, String rentedBy, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate, Boolean returned, OffsetDateTime returnDate, BigDecimal lateReturnWeeklyFine) {
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

        return rentalRepository.updateById(rentalDAO.getId(), rentalDAO);
    }

    public Optional<BigDecimal> getFineById(Long id) {
        Optional<RentalDAO> optionalRental = findById(id);

        // Rental not found
        if (optionalRental.isEmpty()) {
            return Optional.empty();
        }

        RentalDAO rental = optionalRental.get();

        // TODO: Book returned but not paid
        if (rental.isReturned()) {
            return Optional.of(BigDecimal.ZERO);
        }

        // Book not yet returned
        OffsetDateTime currentDate = OffsetDateTime.now();
        OffsetDateTime expectedReturnDate = rental.getExpectedReturnDate();
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
        return OffsetDateTime.now().plusMonths(3);
    }

    private BigDecimal getDefaultLateReturnWeeklyFine() {
        return BigDecimal.ONE;
    }
}
