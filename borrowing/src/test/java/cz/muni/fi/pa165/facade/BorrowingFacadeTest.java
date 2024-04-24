package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.data.model.Borrowing;
import cz.muni.fi.pa165.exceptionhandling.exceptions.ResourceNotFoundException;
import cz.muni.fi.pa165.mappers.BorrowingMapper;
import cz.muni.fi.pa165.service.BorrowingService;
import cz.muni.fi.pa165.util.TestDataFactory;
import cz.muni.fi.pa165.util.TimeProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.BorrowingDTO;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author Sophia Zápotočná
 */
@ExtendWith(MockitoExtension.class)
class BorrowingFacadeTest {
    @Mock
    BorrowingService borrowingService;

    @Mock
    BorrowingMapper borrowingMapper;

    @InjectMocks
    BorrowingFacade borrowingFacade;

    @Test
    void findById_borrowingFound_returnsBorrowing() {
        when(borrowingService.findById(2L)).thenReturn(TestDataFactory.activeBorrowing);
        when(borrowingMapper.mapToDto(TestDataFactory.activeBorrowing)).thenReturn(TestDataFactory.activeBorrowingDTO);

        BorrowingDTO borrowingDTO = borrowingFacade.findById(2L);

        assertThat(borrowingDTO).isEqualTo(TestDataFactory.activeBorrowingDTO);
    }

    @Test
    void findById_borrowingNotFound_throwsResourceNotFoundException() {
        when(borrowingService.findById(11L)).thenThrow(new ResourceNotFoundException(String.format("Borrowing with id: %d not found", 11L)));

        Throwable exception = assertThrows(ResourceNotFoundException.class, () -> borrowingFacade.findById(11L));
        assertThat(exception.getMessage()).isEqualTo(String.format("Borrowing with id: %d not found", 11L));
    }

    @Test
    void createBorrowing_borrowingCreated_returnsNewBorrowing() {
        Long bookId = 17L;
        Long borrowerId = 18L;
        OffsetDateTime expectedReturnDate = OffsetDateTime
                .of(2024, 5, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        BigDecimal lateReturnWeeklyFine = new BigDecimal(100);
        Borrowing newBorrowing = new Borrowing(bookId, borrowerId, null, expectedReturnDate, false,
                null, lateReturnWeeklyFine, false);
        BorrowingDTO newBorrowingDTO = new BorrowingDTO()
                .bookId(bookId)
                .borrowerId(borrowerId)
                .borrowDate(null)
                .expectedReturnDate(expectedReturnDate)
                .returned(false)
                .lateReturnWeeklyFine(lateReturnWeeklyFine)
                .fineResolved(false);


        when(borrowingService.createBorrowing(
                bookId,
                borrowerId,
                expectedReturnDate,
                lateReturnWeeklyFine))
                .thenReturn(newBorrowing);
        when(borrowingMapper.mapToDto(newBorrowing)).thenReturn(newBorrowingDTO);

        BorrowingDTO borrowingDTO = borrowingFacade.createBorrowing(bookId, borrowerId, expectedReturnDate, lateReturnWeeklyFine);

        assertThat(borrowingDTO).isNotNull();
        assertThat(borrowingDTO.getBookId()).isEqualTo(bookId);
        assertThat(borrowingDTO.getBorrowerId()).isEqualTo(borrowerId);
        assertThat(borrowingDTO.getExpectedReturnDate()).isEqualTo(expectedReturnDate);
        assertThat(borrowingDTO.getLateReturnWeeklyFine()).isEqualTo(lateReturnWeeklyFine);
        assertThat(borrowingDTO.getBorrowDate()).isNull();
        assertThat(borrowingDTO.getReturnDate()).isNull();
        assertThat(borrowingDTO.getReturned()).isFalse();
        assertThat(borrowingDTO.getFineResolved()).isFalse();
        verify(borrowingService, times(1)).createBorrowing(
                bookId,
                borrowerId,
                expectedReturnDate,
                lateReturnWeeklyFine);
    }

    @Test
    void deleteById_borrowingDeleted_callsBorrowingRepositoryOneTime() {
        Long idToDelete = 1L;

        borrowingFacade.deleteById(idToDelete);

        verify(borrowingService, times(1)).deleteById(idToDelete);
    }

    @Test
    void findAll_borrowingsReturned_returnsBorrowings() {
        List<Borrowing> borrowings = new ArrayList<>();
        borrowings.add(TestDataFactory.activeBorrowing);
        borrowings.add(TestDataFactory.inActiveBorrowing);
        when(borrowingService.findAll()).thenReturn(borrowings);
        when(borrowingMapper.mapToList(borrowings)).thenReturn(List.of(TestDataFactory.activeBorrowingDTO, TestDataFactory.inAactiveBorrowingDTO));

        List<BorrowingDTO> listOfBorrowings = borrowingFacade.findAll();

        assertThat(listOfBorrowings)
                .isNotNull()
                .hasSize(2)
                .containsExactlyInAnyOrder(TestDataFactory.activeBorrowingDTO, TestDataFactory.inAactiveBorrowingDTO);
    }

    @Test
    void updateById_validParameters_callsUpdateByIdOnService() {
        Borrowing updatedBorrowing = new Borrowing(
                17L,
                18L,
                TimeProvider.now(),
                TimeProvider.now(),
                true,
                TimeProvider.now(),
                new BigDecimal(3),
                true);
        updatedBorrowing.setId(3L);

        when(borrowingService.updateById(
                updatedBorrowing.getId(),
                updatedBorrowing.getBookId(),
                updatedBorrowing.getBorrowerId(),
                updatedBorrowing.getBorrowDate(),
                updatedBorrowing.getExpectedReturnDate(),
                updatedBorrowing.isReturned(),
                updatedBorrowing.getReturnDate(),
                updatedBorrowing.getLateReturnWeeklyFine(),
                updatedBorrowing.isFineResolved()))
                .thenReturn(1);

        int numberOfUpdatedBorrowings = borrowingFacade.updateById(
                updatedBorrowing.getId(),
                updatedBorrowing.getBookId(),
                updatedBorrowing.getBorrowerId(),
                updatedBorrowing.getBorrowDate(),
                updatedBorrowing.getExpectedReturnDate(),
                updatedBorrowing.isReturned(),
                updatedBorrowing.getReturnDate(),
                updatedBorrowing.getLateReturnWeeklyFine(),
                updatedBorrowing.isFineResolved());

        assertThat(numberOfUpdatedBorrowings).isEqualTo(1);
        verify(borrowingService, times(1)).updateById(
                updatedBorrowing.getId(),
                updatedBorrowing.getBookId(),
                updatedBorrowing.getBorrowerId(),
                updatedBorrowing.getBorrowDate(),
                updatedBorrowing.getExpectedReturnDate(),
                updatedBorrowing.isReturned(),
                updatedBorrowing.getReturnDate(),
                updatedBorrowing.getLateReturnWeeklyFine(),
                updatedBorrowing.isFineResolved());
    }

    @Test
    void getFineById_validBorrowing_callsGetFineByIdOnService() {
        when(borrowingService.getFineById(1L)).thenReturn(BigDecimal.ZERO);

        BigDecimal fine = borrowingFacade.getFineById(1L);

        assertThat(fine).isEqualTo(BigDecimal.ZERO);
        verify(borrowingService, times(1)).getFineById(1L);
    }
}
