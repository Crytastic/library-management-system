package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.BorrowingFacade;
import cz.muni.fi.pa165.util.TimeProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.BorrowingDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Sophia Zápotočná
 */
@ExtendWith(MockitoExtension.class)
class BorrowingControllerTest {
    @Mock
    BorrowingFacade borrowingFacade;

    @InjectMocks
    BorrowingController borrowingController;

    @Test
    void findById_borrowingFound_returnsBorrowingAndOkStatus() {
        BorrowingDTO borrowing = createDTOBorrowing();

        when(borrowingFacade.findById(1L)).thenReturn(borrowing);

        ResponseEntity<BorrowingDTO> response = borrowingController.getBorrowing(1L);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(borrowing);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void createBorrowing_borrowingCreated_returnsNewBorrowingAndCreatedStatus() {
        Long bookId = 7L;
        Long borrowerId = 8L;
        OffsetDateTime expectedReturnDate = OffsetDateTime
                .of(2024, 5, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        BigDecimal lateReturnWeeklyFine = new BigDecimal(100);
        BorrowingDTO borrowing = new BorrowingDTO().bookId(bookId).borrowerId(borrowerId).expectedReturnDate(expectedReturnDate)
                .lateReturnWeeklyFine(lateReturnWeeklyFine);

        when(borrowingFacade.createBorrowing(bookId, borrowerId, expectedReturnDate, lateReturnWeeklyFine)).thenReturn(borrowing);

        ResponseEntity<BorrowingDTO> response = borrowingController
                .createBorrowing(bookId, borrowerId, expectedReturnDate, lateReturnWeeklyFine);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(borrowing);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void getBorrowings_validOperation_returnsBorrowingsAndOkStatus() {
        BorrowingDTO borrowingDTO = createDTOBorrowing();
        List<BorrowingDTO> borrowings = new ArrayList<>();
        borrowings.add(borrowingDTO);
        when(borrowingFacade.findAll()).thenReturn(borrowings);

        ResponseEntity<List<BorrowingDTO>> response = borrowingController.getBorrowings();

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(borrowings);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteById_borrowingDelete_callsBorrowingsFacadeDelete() {
        Long idToDelete = 1L;

        borrowingController.deleteBorrowing(idToDelete);

        verify(borrowingFacade, times(1)).deleteById(idToDelete);
    }

    @Test
    void updateBorrowing_borrowingFound_returnsOkStatus() {
        BorrowingDTO borrowingDTO = createDTOBorrowing();
        when(borrowingFacade.updateById(1L,
                borrowingDTO.getBookId(),
                borrowingDTO.getBorrowerId(),
                borrowingDTO.getBorrowDate(),
                borrowingDTO.getExpectedReturnDate(),
                borrowingDTO.getReturned(),
                borrowingDTO.getReturnDate(),
                borrowingDTO.getLateReturnWeeklyFine(),
                borrowingDTO.getFineResolved())).thenReturn(1);

        ResponseEntity<Void> response = borrowingController.updateBorrowing(1L,
                borrowingDTO.getBookId(),
                borrowingDTO.getBorrowerId(),
                borrowingDTO.getBorrowDate(),
                borrowingDTO.getExpectedReturnDate(),
                borrowingDTO.getReturned(),
                borrowingDTO.getReturnDate(),
                borrowingDTO.getLateReturnWeeklyFine(),
                borrowingDTO.getFineResolved());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getFineById_borrowingFound_returnsFineAndOkStatus() {
        when(borrowingFacade.getFineById(1L)).thenReturn(BigDecimal.TWO);

        ResponseEntity<BigDecimal> response = borrowingController.getFineById(1L);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(BigDecimal.TWO);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private static BorrowingDTO createDTOBorrowing() {
        return new BorrowingDTO()
                .bookId(4L)
                .borrowerId(5L)
                .borrowDate(TimeProvider.now())
                .expectedReturnDate(TimeProvider.now().plusMonths(1))
                .returned(false)
                .returnDate(null)
                .lateReturnWeeklyFine(BigDecimal.TWO)
                .fineResolved(false);
    }
}
