package cz.muni.fi.pa165.util;

import cz.muni.fi.pa165.data.model.Rental;
import org.openapitools.model.RentalDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

/**
 * @author Sophia Zápotočná
 */
@Component
public class TestDataFactory {

    public static Rental activeRental = getActiveRentalFactory();
    public static Rental inActiveRental = getInActiveRentalFactory();
    public static Rental activeRentalLate = getActiveLateRentalFactory();
    public static Rental inActiveRentalLate = getInActiveLateRentalFactory();

    public static RentalDTO activeRentalDTO = getActiveRentalDTOFactory();
    public static RentalDTO inAactiveRentalDTO = getInActiveRentalDTOFactory();

    private static Rental getInActiveRentalFactory() {
        OffsetDateTime borrowDate = OffsetDateTime.of(2024, 3, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = OffsetDateTime.of(2024, 4, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        Rental inActiveRental = new Rental(
                "Inactive test book",
                "Rental creator",
                borrowDate,
                expectedReturnDate,
                true,
                expectedReturnDate,
                new BigDecimal(3),
                true);
        inActiveRental.setId(1L);
        return inActiveRental;
    }

    private static Rental getActiveRentalFactory() {
        OffsetDateTime borrowDate = OffsetDateTime.of(2024, 4, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = OffsetDateTime.of(2024, 5, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        Rental activeRental = new Rental(
                "Active test book",
                "Rental creator",
                borrowDate,
                expectedReturnDate,
                false,
                null,
                new BigDecimal(1),
                false);
        activeRental.setId(2L);
        return activeRental;
    }

    private static Rental getInActiveLateRentalFactory() {
        OffsetDateTime borrowDate = OffsetDateTime.of(2024, 3, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = OffsetDateTime.of(2024, 4, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        Rental inActiveRental = new Rental(
                "Inactive test book",
                "Rental creator",
                borrowDate,
                expectedReturnDate,
                true,
                expectedReturnDate.plusWeeks(4),
                new BigDecimal(3),
                false);
        inActiveRental.setId(1L);
        return inActiveRental;
    }

    private static Rental getActiveLateRentalFactory() {
        OffsetDateTime borrowDate = OffsetDateTime.of(2024, 2, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = OffsetDateTime.of(2024, 3, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        Rental activeRental = new Rental(
                "Active test book",
                "Rental creator",
                borrowDate,
                expectedReturnDate,
                false,
                null,
                new BigDecimal(1),
                false);
        activeRental.setId(2L);
        return activeRental;
    }

    private static RentalDTO getInActiveRentalDTOFactory() {
        OffsetDateTime borrowDate = OffsetDateTime.of(2024, 3, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = OffsetDateTime.of(2024, 4, 1, 12, 0, 0, 0, ZoneOffset.UTC);

        return new RentalDTO().
                book("Inactive test book").
                rentedBy("Rental creator").
                borrowDate(borrowDate).
                expectedReturnDate(expectedReturnDate).
                returned(true).
                returnDate(expectedReturnDate).
                lateReturnWeeklyFine(new BigDecimal(3)).
                fineResolved(true).
                id(2L);
    }

    private static RentalDTO getActiveRentalDTOFactory() {
        OffsetDateTime borrowDate = OffsetDateTime.of(2024, 4, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = OffsetDateTime.of(2024, 5, 1, 12, 0, 0, 0, ZoneOffset.UTC);

        return new RentalDTO()
                .book("Active test book")
                .rentedBy("Rental creator")
                .borrowDate(borrowDate)
                .expectedReturnDate(expectedReturnDate)
                .returned(false)
                .returnDate(null)
                .lateReturnWeeklyFine(new BigDecimal(1))
                .fineResolved(false);
    }


}
