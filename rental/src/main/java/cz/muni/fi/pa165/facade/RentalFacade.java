package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.data.model.Rental;
import cz.muni.fi.pa165.service.RentalService;
import org.openapitools.model.RentalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Facade layer for managing rental.
 * Provides methods for interacting with rental properties.
 *
 * @author Maxmilián Šeffer
 */
@Service
public class RentalFacade {

    private final RentalService rentalService;

    @Autowired
    public RentalFacade(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    public List<RentalDTO> findAll() {
        return rentalService
                .findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RentalDTO createRental(String book, String rentedBy, OffsetDateTime expectedReturnDate, BigDecimal lateReturnWeeklyFine) {
        Rental rental = rentalService.createRental(book, rentedBy, expectedReturnDate, lateReturnWeeklyFine);
        return convertToDTO(rental);
    }

    public Optional<RentalDTO> findById(Long id) {
        return rentalService.findById(id).map(this::convertToDTO);
    }

    public boolean deleteById(Long id) {
        return rentalService.deleteById(id);
    }

    public Optional<RentalDTO> updateById(Long id, String book, String rentedBy, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate, Boolean returned, OffsetDateTime returnDate, BigDecimal lateReturnWeeklyFine, Boolean fineResolved) {
        return rentalService.updateById(id, book, rentedBy, borrowDate, expectedReturnDate, returned, returnDate, lateReturnWeeklyFine, fineResolved).map(this::convertToDTO);
    }

    public Optional<BigDecimal> getFineById(Long id) {
        return rentalService.getFineById(id);
    }

    private RentalDTO convertToDTO(Rental rental) {
        return new RentalDTO()
                .id(rental.getId())
                .book(rental.getBook())
                .rentedBy(rental.getRentedBy())
                .borrowDate(rental.getBorrowDate())
                .expectedReturnDate(rental.getExpectedReturnDate())
                .returned(rental.isReturned())
                .returnDate(rental.getReturnDate())
                .lateReturnWeeklyFine(rental.getLateReturnWeeklyFine())
                .fineResolved(rental.isFineResolved());
    }
}