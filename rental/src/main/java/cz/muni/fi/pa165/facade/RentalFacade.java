package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.mappers.RentalMapper;
import cz.muni.fi.pa165.service.RentalService;
import org.openapitools.model.RentalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Facade layer for managing rental.
 * Provides methods for interacting with rental properties.
 *
 * @author Maxmilián Šeffer
 */
@Service
public class RentalFacade {

    private final RentalService rentalService;

    private final RentalMapper rentalMapper;

    @Autowired
    public RentalFacade(RentalService rentalService, RentalMapper rentalMapper) {
        this.rentalService = rentalService;
        this.rentalMapper = rentalMapper;
    }

    public List<RentalDTO> findAll() {
        return rentalMapper.mapToList(rentalService.findAll());
    }

    public RentalDTO createRental(Long bookId, Long borrowerId, OffsetDateTime expectedReturnDate, BigDecimal lateReturnWeeklyFine) {
        return rentalMapper.mapToDto(rentalService.createRental(bookId, borrowerId, expectedReturnDate, lateReturnWeeklyFine));
    }

    public Optional<RentalDTO> findById(Long id) {
        return rentalService.findById(id).map(rentalMapper::mapToDto);
    }

    public void deleteById(Long id) {
        rentalService.deleteById(id);
    }

    public int updateById(Long id, Long bookId, Long borrowerId, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate, Boolean returned, OffsetDateTime returnDate, BigDecimal lateReturnWeeklyFine, Boolean fineResolved) {
        return rentalService.updateById(id, bookId, borrowerId, borrowDate, expectedReturnDate, returned, returnDate, lateReturnWeeklyFine, fineResolved);
    }

    public Optional<BigDecimal> getFineById(Long id) {
        return rentalService.getFineById(id);
    }

}