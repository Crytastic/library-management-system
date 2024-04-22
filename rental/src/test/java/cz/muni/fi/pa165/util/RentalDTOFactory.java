package cz.muni.fi.pa165.util;

import org.openapitools.model.RentalDTO;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class RentalDTOFactory {
    public static RentalDTO createRental(Long id, String book, String rentedBy, OffsetDateTime borrowDate,
                                         OffsetDateTime expectedReturnDate, Boolean returned, OffsetDateTime returnDate,
                                         BigDecimal lateReturnWeeklyFine, Boolean fineResolved) {
        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setId(id);
        rentalDTO.setBook(book);
        rentalDTO.setRentedBy(rentedBy);
        rentalDTO.setBorrowDate(borrowDate);
        rentalDTO.setExpectedReturnDate(expectedReturnDate);
        rentalDTO.setReturned(returned);
        rentalDTO.setReturnDate(returnDate);
        rentalDTO.setLateReturnWeeklyFine(lateReturnWeeklyFine);
        rentalDTO.setFineResolved(fineResolved);
        return rentalDTO;
    }
}