package cz.muni.fi.pa165.data.repository;

import cz.muni.fi.pa165.data.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Modifying
    @Query("UPDATE Rental rental SET " +
            "rental.bookId = coalesce(:bookId, rental.bookId), " +
            "rental.borrowerId = coalesce(:borrowerId, rental.borrowerId)," +
            "rental.borrowDate = coalesce(:borrowDate, rental.borrowDate)," +
            "rental.expectedReturnDate = coalesce(:expectedReturnDate, rental.expectedReturnDate)," +
            "rental.returned = coalesce(:returned, rental.returned)," +
            "rental.returnDate = coalesce(:returnDate, rental.returnDate)," +
            "rental.lateReturnWeeklyFine = coalesce(:lateReturnWeeklyFine, rental.lateReturnWeeklyFine)," +
            "rental.fineResolved = coalesce(:fineResolved, rental.fineResolved)" +
            " WHERE rental.id = :id")
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