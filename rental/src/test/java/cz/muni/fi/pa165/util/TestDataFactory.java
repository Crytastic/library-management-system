package cz.muni.fi.pa165.util;

import cz.muni.fi.pa165.dao.RentalDAO;
import org.openapitools.model.RentalDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
public class TestDataFactory {

    public static RentalDAO activeRentalDAO = getActiveRentalDAOFactory();
    public static RentalDAO inActiveRentalDAO = getInActiveRentalDAOFactory();
    public static RentalDAO activeRentalLateDAO = getActiveLateRentalDAOFactory();
    public static RentalDAO inActiveRentalLateDAO = getInActiveLateRentalDAOFactory();

    public static RentalDTO activeRentalDTO = getActiveRentalDTOFactory();

    public static RentalDTO inActiveRentalDTO = getInActiveRentalDTOFactory();



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

    private static RentalDTO getInActiveRentalDTOFactory() {
        OffsetDateTime borrowDate = OffsetDateTime.of(2024, 3, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = OffsetDateTime.of(2024, 4, 1, 12, 0, 0, 0, ZoneOffset.UTC);

        return new RentalDTO()
                .book("Inactive test book")
                .rentedBy("Rental creator")
                .borrowDate(borrowDate)
                .expectedReturnDate(expectedReturnDate)
                .returned(true)
                .returnDate(expectedReturnDate)
                .lateReturnWeeklyFine(new BigDecimal(3))
                .fineResolved(true);
    }

    private static RentalDAO getInActiveRentalDAOFactory() {
        OffsetDateTime borrowDate = OffsetDateTime.of(2024, 3, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = OffsetDateTime.of(2024, 4, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        RentalDAO inActiveRental = new RentalDAO(
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

    private static RentalDAO getActiveRentalDAOFactory() {
        OffsetDateTime borrowDate = OffsetDateTime.of(2024, 4, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = OffsetDateTime.of(2024, 5, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        RentalDAO activeRental = new RentalDAO(
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

    private static RentalDAO getInActiveLateRentalDAOFactory() {
        OffsetDateTime borrowDate = OffsetDateTime.of(2024, 3, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = OffsetDateTime.of(2024, 4, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        RentalDAO inActiveRental = new RentalDAO(
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

    private static RentalDAO getActiveLateRentalDAOFactory() {
        OffsetDateTime borrowDate = OffsetDateTime.of(2024, 2, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = OffsetDateTime.of(2024, 3, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        RentalDAO activeRental = new RentalDAO(
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

}
