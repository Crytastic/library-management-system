package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.mappers.BorrowingMapper;
import cz.muni.fi.pa165.service.BorrowingService;
import org.openapitools.model.BorrowingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Facade layer for managing borrowing.
 * Provides methods for interacting with borrowing properties.
 *
 * @author Maxmilián Šeffer
 */
@Service
public class BorrowingFacade {

    private final BorrowingService borrowingService;

    private final BorrowingMapper borrowingMapper;

    @Autowired
    public BorrowingFacade(BorrowingService borrowingService, BorrowingMapper borrowingMapper) {
        this.borrowingService = borrowingService;
        this.borrowingMapper = borrowingMapper;
    }

    public List<BorrowingDTO> findAll() {
        return borrowingMapper.mapToList(borrowingService.findAll());
    }

    public BorrowingDTO createBorrowing(Long bookId, Long borrowerId, OffsetDateTime expectedReturnDate, BigDecimal lateReturnWeeklyFine) {
        return borrowingMapper.mapToDto(borrowingService.createBorrowing(bookId, borrowerId, expectedReturnDate, lateReturnWeeklyFine));
    }

    public Optional<BorrowingDTO> findById(Long id) {
        return borrowingService.findById(id).map(borrowingMapper::mapToDto);
    }

    public void deleteById(Long id) {
        borrowingService.deleteById(id);
    }

    public int updateById(Long id, Long bookId, Long borrowerId, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate, Boolean returned, OffsetDateTime returnDate, BigDecimal lateReturnWeeklyFine, Boolean fineResolved) {
        return borrowingService.updateById(id, bookId, borrowerId, borrowDate, expectedReturnDate, returned, returnDate, lateReturnWeeklyFine, fineResolved);
    }

    public Optional<BigDecimal> getFineById(Long id) {
        return borrowingService.getFineById(id);
    }

}