package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.mappers.BorrowingMapper;
import cz.muni.fi.pa165.service.BorrowingService;
import org.openapitools.model.BorrowingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

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

    public BorrowingDTO findById(Long id) {
        return borrowingMapper.mapToDto(borrowingService.findById(id));
    }

    public void deleteById(Long id) {
        borrowingService.deleteById(id);
    }

    public BorrowingDTO updateById(Long id, Long bookId, Long borrowerId, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate, Boolean returned, OffsetDateTime returnDate, BigDecimal lateReturnWeeklyFine, Boolean fineResolved) {
        return borrowingMapper.mapToDto(borrowingService.updateById(id, bookId, borrowerId, borrowDate, expectedReturnDate, returned, returnDate, lateReturnWeeklyFine, fineResolved));
    }

    public BigDecimal getFineById(Long id) {
        return borrowingService.getFineById(id);
    }

    public void deleteAll() {
        borrowingService.deleteAll();
    }

    public List<BorrowingDTO> findAllActive() {
        return borrowingMapper.mapToList(borrowingService.findAllActive());
    }

    public List<BorrowingDTO> findAllByBook(Long id) {
        return borrowingMapper.mapToList(borrowingService.findAllByBook(id));
    }
}
