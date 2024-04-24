package cz.muni.fi.pa165.util;

import org.openapitools.model.BorrowingDTO;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class BorrowingDTOFactory {
    public static BorrowingDTO createBorrowing(Long id, Long bookId, Long borrowerId, OffsetDateTime borrowDate,
                                               OffsetDateTime expectedReturnDate, Boolean returned, OffsetDateTime returnDate,
                                               BigDecimal lateReturnWeeklyFine, Boolean fineResolved) {
        BorrowingDTO borrowingDTO = new BorrowingDTO();
        borrowingDTO.setId(id);
        borrowingDTO.setBookId(bookId);
        borrowingDTO.setBorrowerId(borrowerId);
        borrowingDTO.setBorrowDate(borrowDate);
        borrowingDTO.setExpectedReturnDate(expectedReturnDate);
        borrowingDTO.setReturned(returned);
        borrowingDTO.setReturnDate(returnDate);
        borrowingDTO.setLateReturnWeeklyFine(lateReturnWeeklyFine);
        borrowingDTO.setFineResolved(fineResolved);
        return borrowingDTO;
    }
}
