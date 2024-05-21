package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.Borrowing;
import cz.muni.fi.pa165.data.repository.BorrowingRepository;
import cz.muni.fi.pa165.exceptionhandling.exceptions.ConstraintViolationException;
import cz.muni.fi.pa165.exceptionhandling.exceptions.ResourceNotFoundException;
import cz.muni.fi.pa165.util.ServiceHttpRequestProvider;
import cz.muni.fi.pa165.util.TestDataFactory;
import cz.muni.fi.pa165.util.TimeProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author Sophia Zápotočná
 */
@ExtendWith(MockitoExtension.class)
class BorrowingServiceTest {
    @Mock
    private BorrowingRepository borrowingRepository;

    @Mock
    private ServiceHttpRequestProvider serviceHttpRequestProvider;

    @InjectMocks
    private BorrowingService borrowingService;

    @Test
    void findById_borrowingFound_returnsBorrowing() {
        when(borrowingRepository.findById(1L)).thenReturn(Optional.ofNullable(TestDataFactory.activeBorrowing));

        Borrowing found = borrowingService.findById(1L);

        assertThat(found).isEqualTo(TestDataFactory.activeBorrowing);
    }

    @Test
    void findById_borrowingNotFound_throwsResourceNotFoundException() {
        when(borrowingRepository.findById(11L)).thenThrow(new ResourceNotFoundException(String.format("Borrowing with id: %d not found", 11L)));

        Throwable exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.findById(11L));
        assertThat(exception.getMessage()).isEqualTo(String.format("Borrowing with id: %d not found", 11L));
    }

    @Test
    void createBorrowing_borrowingCreated_returnsNewBorrowing() {
        Long bookId = 7L;
        Long borrowerId = 8L;
        OffsetDateTime borrowDate = OffsetDateTime
                .of(2024, 5, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = borrowDate.plusWeeks(6);
        BigDecimal lateReturnWeeklyFine = new BigDecimal(100);
        Borrowing newBorrowing = new Borrowing(bookId, borrowerId, borrowDate, expectedReturnDate, false,
                null, lateReturnWeeklyFine, false);
        when(borrowingRepository.save(newBorrowing)).thenReturn(newBorrowing);
        when(serviceHttpRequestProvider.callGetBookById(7L)).thenReturn(new ResponseEntity<>("something", HttpStatus.OK));
        when(serviceHttpRequestProvider.callGetUserById(8L)).thenReturn(new ResponseEntity<>("something", HttpStatus.OK));

        try (MockedStatic<TimeProvider> timeProviderDummy = mockStatic(TimeProvider.class)) {
            timeProviderDummy.when(TimeProvider::now).thenReturn(borrowDate);
            Borrowing borrowing = borrowingService.createBorrowing(bookId, borrowerId, expectedReturnDate, lateReturnWeeklyFine);

            assertThat(borrowing).isNotNull().isEqualTo(newBorrowing);
            assertThat(borrowing.getBookId()).isEqualTo(bookId);
            assertThat(borrowing.getBorrowerId()).isEqualTo(borrowerId);
            assertThat(borrowing.getExpectedReturnDate()).isEqualTo(expectedReturnDate);
            assertThat(borrowing.getLateReturnWeeklyFine()).isEqualTo(lateReturnWeeklyFine);
            assertThat(borrowing.getBorrowDate()).isEqualTo(borrowDate);
            assertThat(borrowing.getReturnDate()).isNull();
            assertThat(borrowing.isReturned()).isFalse();
            assertThat(borrowing.isFineResolved()).isFalse();
            verify(borrowingRepository, times(1)).save(newBorrowing);
        }
    }

    @Test
    void createBorrowing_bookAlreadyBorrowed_throwsConstraintViolationException() {
        // Arrange
        Long bookId = 1L;
        Long borrowerId = 2L;
        OffsetDateTime now = TimeProvider.now();
        Borrowing activeBorrowing = new Borrowing(bookId, borrowerId, now.minusDays(1), now.plusDays(14), false, null, BigDecimal.TEN, false);

        when(borrowingRepository.findAll()).thenReturn(List.of(activeBorrowing));

        // Act
        Throwable exception = assertThrows(ConstraintViolationException.class, () -> borrowingService.createBorrowing(bookId, borrowerId, null, null));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Book already borrowed.");
    }

    @Test
    void deleteById_borrowingDeleted_callsBorrowingRepositoryOneTime() {
        Long idToDelete = 1L;

        borrowingService.deleteById(idToDelete);

        verify(borrowingRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void deleteAll_allBorrowingsDeleted_callsBorrowingRepositoryOneTime() {
        borrowingService.deleteAll();

        verify(borrowingRepository, times(1)).deleteAll();
    }

    @Test
    void findAll_borrowingsReturned_returnsBorrowings() {
        List<Borrowing> borrowings = new ArrayList<>();
        borrowings.add(TestDataFactory.activeBorrowing);
        borrowings.add(TestDataFactory.inActiveBorrowing);
        when(borrowingRepository.findAll()).thenReturn(borrowings);

        List<Borrowing> listOfBorrowings = borrowingService.findAll();

        assertThat(listOfBorrowings)
                .isNotNull()
                .hasSize(2)
                .containsExactlyInAnyOrder(TestDataFactory.activeBorrowing, TestDataFactory.inActiveBorrowing);
    }

    @Test
    void updateById_oneItemChanged_returnsUpdatedBorrowing() {
        Long changedBook = 2L;
        Borrowing updatedBorrowing = TestDataFactory.activeBorrowing;
        when(borrowingRepository.updateById(updatedBorrowing.getId(), changedBook, null, null,
                null, null, null, null, null))
                .thenReturn(1);
        when(borrowingRepository.findById(TestDataFactory.activeBorrowing.getId())).thenReturn(Optional.of(updatedBorrowing));

        Borrowing result = borrowingService.updateById(updatedBorrowing.getId(), changedBook, null, null,
                null, null, null, null, null);

        assertThat(result).isEqualTo(TestDataFactory.activeBorrowing);
        verify(borrowingRepository, times(1)).updateById(updatedBorrowing.getId(), changedBook, null, null,
                null, null, null, null, null);
    }

    @Test
    void updateById_someItemChanged_returnsOne() {
        Long changedBook = 3L;
        Boolean returned = true;
        OffsetDateTime returnDate = TimeProvider.now();
        Borrowing updatedBorrowing = TestDataFactory.activeBorrowing;
        when(borrowingRepository.updateById(updatedBorrowing.getId(), changedBook, null, null,
                null, returned, returnDate, null, null))
                .thenReturn(1);


        int numberOfUpdatedBorrowings = borrowingRepository.updateById(updatedBorrowing.getId(), changedBook, null, null,
                null, returned, returnDate, null, null);


        assertThat(numberOfUpdatedBorrowings).isEqualTo(1);
        verify(borrowingRepository, times(1)).updateById(updatedBorrowing.getId(), changedBook, null, null,
                null, returned, returnDate, null, null);
    }

    @Test
    void updateById_allItemChanged_returnsUpdatedborrowing() {
        Long changedBook = 4L;
        Long borrowerId = 5L;
        Boolean returned = true;
        OffsetDateTime returnedDate = TimeProvider.now();
        OffsetDateTime expectedReturnDate = TimeProvider.now();
        OffsetDateTime borrowDate = TimeProvider.now().minusMonths(1L);
        Boolean fineResolved = true;
        BigDecimal lateReturnWeeklyFine = new BigDecimal(2);
        Borrowing updatedBorrowing = TestDataFactory.activeBorrowing;
        when(borrowingRepository.updateById(updatedBorrowing.getId(), changedBook, borrowerId,
                borrowDate, expectedReturnDate, returned, returnedDate, lateReturnWeeklyFine, fineResolved)).thenReturn(1);
        when(borrowingRepository.findById(updatedBorrowing.getId())).thenReturn(Optional.of(TestDataFactory.activeBorrowing));

        Borrowing result = borrowingService.updateById(updatedBorrowing.getId(), changedBook, borrowerId,
                borrowDate, expectedReturnDate, returned, returnedDate, lateReturnWeeklyFine, fineResolved);

        assertThat(result).isEqualTo(TestDataFactory.activeBorrowing);
        verify(borrowingRepository, times(1)).updateById(updatedBorrowing.getId(), changedBook, borrowerId,
                borrowDate, expectedReturnDate, returned, returnedDate, lateReturnWeeklyFine, fineResolved);
    }

    @Test
    void updateById_borrowingNotFound_throwsResourceNotFoundException() {
        Long nonExistingId = 11L;
        Long changedBook = 3L;
        when(borrowingRepository.updateById(nonExistingId, changedBook, null, null, null, null, null, null, null)).thenReturn(0);

        Throwable exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.updateById(nonExistingId, changedBook, null, null,
                null, null, null, null, null));

        assertThat(exception.getMessage()).isEqualTo(String.format("Borrowing with id: %d not found", nonExistingId));

        verify(borrowingRepository, times(1)).updateById(nonExistingId, changedBook, null, null, null, null, null, null, null);
    }

    @Test
    void getFineById_borrowingNotFound_returnsEmptyOptional() {
        Long nonExistingId = 11L;
        when(borrowingRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.getFineById(nonExistingId));

        assertThat(exception.getMessage()).isEqualTo(String.format("Borrowing with id: %d not found", nonExistingId));
        verify(borrowingRepository, times(1)).findById(nonExistingId);
    }

    @Test
    void getFineById_bookReturnedFinePaid_returnsZeroFine() {
        Borrowing inActiveBorrowing = TestDataFactory.inActiveBorrowing;
        when(borrowingRepository.findById(inActiveBorrowing.getId())).thenReturn(Optional.of(inActiveBorrowing));

        BigDecimal fine = borrowingService.getFineById(inActiveBorrowing.getId());

        assertThat(fine).isEqualTo(BigDecimal.ZERO);
        verify(borrowingRepository, times(1)).findById(inActiveBorrowing.getId());
    }

    @Test
    void getFineById_bookReturnedFineNotPaid_returnsSomeFine() {
        Borrowing inActiveBorrowing = TestDataFactory.inActiveBorrowingLate;
        when(borrowingRepository.findById(inActiveBorrowing.getId())).thenReturn(Optional.of(inActiveBorrowing));

        BigDecimal fine = borrowingService.getFineById(inActiveBorrowing.getId());

        assertThat(fine).isEqualTo(new BigDecimal(12));
        verify(borrowingRepository, times(1)).findById(inActiveBorrowing.getId());
    }

    @Test
    void getFineById_bookNotReturnedStillHaveTime_returnsZeroFine() {
        Borrowing activeBorrowing = TestDataFactory.activeBorrowing;
        when(borrowingRepository.findById(activeBorrowing.getId())).thenReturn(Optional.of(activeBorrowing));

        try (MockedStatic<TimeProvider> timeProviderDummy = mockStatic(TimeProvider.class)) {
            timeProviderDummy.when(TimeProvider::now).thenReturn(activeBorrowing.getExpectedReturnDate().minusHours(1));

            BigDecimal fine = borrowingService.getFineById(activeBorrowing.getId());

            assertThat(fine).isEqualTo(BigDecimal.ZERO);
            verify(borrowingRepository, times(1)).findById(activeBorrowing.getId());
        }
    }

    @Test
    void getFineById_bookNotReturnedNowIsLate_returnsSomeFine() {
        Borrowing activeBorrowing = TestDataFactory.activeBorrowingLate;
        when(borrowingRepository.findById(activeBorrowing.getId())).thenReturn(Optional.of(activeBorrowing));

        try (MockedStatic<TimeProvider> timeProviderDummy = mockStatic(TimeProvider.class)) {
            timeProviderDummy.when(TimeProvider::now).thenReturn(activeBorrowing.getExpectedReturnDate().plusWeeks(5));

            BigDecimal fine = borrowingService.getFineById(activeBorrowing.getId());

            assertThat(fine).isEqualTo(new BigDecimal(5));
            verify(borrowingRepository, times(1)).findById(activeBorrowing.getId());
        }
    }

    @Test
    void getFineById_bookNotFound_throwsResourceNotfoundException() {
        when(borrowingRepository.findById(1000L)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(ResourceNotFoundException.class, () -> borrowingService.getFineById(1000L));
        assertThat(exception.getMessage()).isEqualTo(String.format("Borrowing with id: %d not found", 1000L));
    }

    @Test
    void getAllActive_activeBorrowingPresent_returnsActiveBorrowings() {
        when(borrowingRepository.findAll()).thenReturn(List.of(TestDataFactory.activeBorrowing, TestDataFactory.inActiveBorrowing));

        List<Borrowing> borrowings = borrowingService.findAllActive();
        assertThat(borrowings).hasSize(1).first().isEqualTo(TestDataFactory.activeBorrowing);
    }

    @Test
    void getAllByBook_someBorrowingWithBookExists_returnsBorrowings() {
        when(borrowingRepository.findAll()).thenReturn(List.of(TestDataFactory.activeBorrowing, TestDataFactory.inActiveBorrowing));

        List<Borrowing> borrowings = borrowingService.findAllByBook(27L);
        assertThat(borrowings).hasSize(1).first().isEqualTo(TestDataFactory.activeBorrowing);
    }
}
