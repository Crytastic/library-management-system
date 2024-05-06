package cz.muni.fi.pa165.util;

import cz.muni.fi.pa165.data.model.Borrowing;
import org.openapitools.model.BorrowingDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * @author Sophia Zápotočná
 */
@Component
public class TestDataFactory {
    public static Borrowing activeBorrowing = getActiveBorrowingFactory();
    public static Borrowing inActiveBorrowing = getInActiveBorrowingFactory();
    public static Borrowing activeBorrowingLate = getActiveLateBorrowingFactory();
    public static Borrowing inActiveBorrowingLate = getInActiveLateBorrowingFactory();

    public static BorrowingDTO activeBorrowingDTO = getActiveBorrowingDTOFactory();
    public static BorrowingDTO inAactiveBorrowingDTO = getInActiveBorrowingDTOFactory();

    private static Borrowing getInActiveBorrowingFactory() {
        OffsetDateTime borrowDate = OffsetDateTime.of(2024, 3, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = OffsetDateTime.of(2024, 4, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        Borrowing inActiveBorrowing = new Borrowing(
                17L,
                18L,
                borrowDate,
                expectedReturnDate,
                true,
                expectedReturnDate,
                new BigDecimal(3),
                true);
        inActiveBorrowing.setId(1L);
        return inActiveBorrowing;
    }

    private static Borrowing getActiveBorrowingFactory() {
        OffsetDateTime borrowDate = OffsetDateTime.of(2024, 4, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = OffsetDateTime.of(2024, 6, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        Borrowing activeBorrowing = new Borrowing(
                27L,
                28L,
                borrowDate,
                expectedReturnDate,
                false,
                null,
                new BigDecimal(1),
                false);
        activeBorrowing.setId(2L);
        return activeBorrowing;
    }

    private static Borrowing getInActiveLateBorrowingFactory() {
        OffsetDateTime borrowDate = OffsetDateTime.of(2024, 3, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = OffsetDateTime.of(2024, 4, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        Borrowing inActiveBorrowing = new Borrowing(
                37L,
                38L,
                borrowDate,
                expectedReturnDate,
                true,
                expectedReturnDate.plusWeeks(4),
                new BigDecimal(3),
                false);
        inActiveBorrowing.setId(1L);
        return inActiveBorrowing;
    }

    private static Borrowing getActiveLateBorrowingFactory() {
        OffsetDateTime borrowDate = OffsetDateTime.of(2024, 2, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = OffsetDateTime.of(2024, 3, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        Borrowing activeBorrowing = new Borrowing(
                47L,
                48L,
                borrowDate,
                expectedReturnDate,
                false,
                null,
                new BigDecimal(1),
                false);
        activeBorrowing.setId(2L);
        return activeBorrowing;
    }

    private static BorrowingDTO getInActiveBorrowingDTOFactory() {
        OffsetDateTime borrowDate = OffsetDateTime.of(2024, 3, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = OffsetDateTime.of(2024, 4, 1, 12, 0, 0, 0, ZoneOffset.UTC);

        return new BorrowingDTO().
                bookId(17L).
                borrowerId(18L).
                borrowDate(borrowDate).
                expectedReturnDate(expectedReturnDate).
                returned(true).
                returnDate(expectedReturnDate).
                lateReturnWeeklyFine(new BigDecimal(3)).
                fineResolved(true).
                id(2L);
    }

    private static BorrowingDTO getActiveBorrowingDTOFactory() {
        OffsetDateTime borrowDate = OffsetDateTime.of(2024, 4, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = OffsetDateTime.of(2024, 5, 1, 12, 0, 0, 0, ZoneOffset.UTC);

        return new BorrowingDTO()
                .bookId(27L)
                .borrowerId(28L)
                .borrowDate(borrowDate)
                .expectedReturnDate(expectedReturnDate)
                .returned(false)
                .returnDate(null)
                .lateReturnWeeklyFine(new BigDecimal(1))
                .fineResolved(false);
    }
}
