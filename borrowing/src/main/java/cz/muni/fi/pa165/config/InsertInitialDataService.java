package cz.muni.fi.pa165.config;

import cz.muni.fi.pa165.data.model.Borrowing;
import cz.muni.fi.pa165.data.repository.BorrowingRepository;
import cz.muni.fi.pa165.util.TimeProvider;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Service
@Transactional
public class InsertInitialDataService {

    private final BorrowingRepository borrowingRepository;

    @Autowired
    public InsertInitialDataService(BorrowingRepository borrowingRepository) {
        this.borrowingRepository = borrowingRepository;
    }

    @PostConstruct
    public void insertInitialData() {
        borrowingRepository.save(bookId2BorrowedById5());
        borrowingRepository.save(bookId7BorrowedById3());
        borrowingRepository.save(bookId9BorrowedById4());
    }
    private Borrowing bookId2BorrowedById5() {
        Long bookId = 2L;
        Long borrowerId = 5L;
        OffsetDateTime borrowDate = TimeProvider.now();
        OffsetDateTime expectedReturnDate = TimeProvider.now().plusMonths(1);
        boolean returned = false;
        OffsetDateTime returnDate = null;
        BigDecimal lateReturnWeeklyFine = BigDecimal.TWO;
        boolean fineResolved = false;

        return new Borrowing(bookId, borrowerId, borrowDate, expectedReturnDate, returned, returnDate, lateReturnWeeklyFine,
                fineResolved);
    }

    private Borrowing bookId7BorrowedById3() {
        Long bookId = 7L;
        Long borrowerId = 3L;
        OffsetDateTime borrowDate = TimeProvider.now().minusDays(3);
        OffsetDateTime expectedReturnDate = TimeProvider.now().plusMonths(1).minusDays(3);
        boolean returned = false;
        OffsetDateTime returnDate = null;
        BigDecimal lateReturnWeeklyFine = BigDecimal.ONE;
        boolean fineResolved = false;

        return new Borrowing(bookId, borrowerId, borrowDate, expectedReturnDate, returned, returnDate, lateReturnWeeklyFine,
                fineResolved);
    }

    private Borrowing bookId9BorrowedById4() {
        Long bookId = 9L;
        Long borrowerId = 4L;
        OffsetDateTime borrowDate = TimeProvider.now().minusWeeks(2);
        OffsetDateTime expectedReturnDate = TimeProvider.now().plusMonths(1).minusWeeks(1);
        boolean returned = false;
        OffsetDateTime returnDate = null;
        BigDecimal lateReturnWeeklyFine = BigDecimal.TWO;
        boolean fineResolved = false;

        return new Borrowing(bookId, borrowerId, borrowDate, expectedReturnDate, returned, returnDate, lateReturnWeeklyFine,
                fineResolved);
    }
}
