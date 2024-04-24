package cz.muni.fi.pa165.data.repository;

import cz.muni.fi.pa165.data.model.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Repository layer for managing book borrowings.
 * Provides methods for storing, retrieving and updating the entity borrowing.
 *
 * @author Maxmilián Šeffer
 */
@Repository
public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {
    @Modifying
    @Query("UPDATE Borrowing borrowing SET " +
            "borrowing.bookId = coalesce(:bookId, borrowing.bookId), " +
            "borrowing.borrowerId = coalesce(:borrowerId, borrowing.borrowerId)," +
            "borrowing.borrowDate = coalesce(:borrowDate, borrowing.borrowDate)," +
            "borrowing.expectedReturnDate = coalesce(:expectedReturnDate, borrowing.expectedReturnDate)," +
            "borrowing.returned = coalesce(:returned, borrowing.returned)," +
            "borrowing.returnDate = coalesce(:returnDate, borrowing.returnDate)," +
            "borrowing.lateReturnWeeklyFine = coalesce(:lateReturnWeeklyFine, borrowing.lateReturnWeeklyFine)," +
            "borrowing.fineResolved = coalesce(:fineResolved, borrowing.fineResolved)" +
            " WHERE borrowing.id = :id")
    int updateById(Long id,
                   Long bookId,
                   Long borrowerId,
                   OffsetDateTime borrowDate,
                   OffsetDateTime expectedReturnDate,
                   Boolean returned,
                   OffsetDateTime returnDate,
                   BigDecimal lateReturnWeeklyFine,
                   Boolean fineResolved
    );
}
