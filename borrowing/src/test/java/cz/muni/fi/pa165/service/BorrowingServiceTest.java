package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.Borrowing;
import cz.muni.fi.pa165.data.repository.BorrowingRepository;
import cz.muni.fi.pa165.util.TestDataFactory;
import cz.muni.fi.pa165.util.TimeProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Sophia Zápotočná
 */
@ExtendWith(MockitoExtension.class)
class BorrowingServiceTest {
    @Mock
    private BorrowingRepository borrowingRepository;

    @InjectMocks
    private BorrowingService borrowingService;

    @Test
    void findById_borrowingFound_returnsBorrowing() {
        when(borrowingRepository.findById(1L)).thenReturn(Optional.ofNullable(TestDataFactory.activeBorrowing));

        Optional<Borrowing> found = borrowingService.findById(1L);

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(TestDataFactory.activeBorrowing);
    }

    @Test
    void findById_borrowingNotFound_returnsEmptyOptional() {
        when(borrowingRepository.findById(11L)).thenReturn(Optional.empty());

        Optional<Borrowing> found = borrowingService.findById(11L);

        assertThat(found).isEmpty();
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
    void deleteById_borrowingDeleted_callsBorrowingRepositoryOneTime() {
        Long idToDelete = 1L;

        borrowingService.deleteById(idToDelete);

        verify(borrowingRepository, times(1)).deleteById(idToDelete);
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
    void updateById_oneItemChanged_returnsOne() {
        Long changedBook = 2L;
        Borrowing updatedBorrowing = TestDataFactory.activeBorrowing;
        when(borrowingRepository.updateById(updatedBorrowing.getId(), changedBook, null, null,
                null, null, null, null, null))
                .thenReturn(1);

        int numberOfUpdatedBorrowings = borrowingService.updateById(updatedBorrowing.getId(), changedBook, null, null,
                null, null, null, null, null);

        assertThat(numberOfUpdatedBorrowings).isEqualTo(1);
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
    void updateById_allItemChanged_returnsOne() {
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

        int numberOfUpdatedBorrowings = borrowingService.updateById(updatedBorrowing.getId(), changedBook, borrowerId,
                borrowDate, expectedReturnDate, returned, returnedDate, lateReturnWeeklyFine, fineResolved);

        assertThat(numberOfUpdatedBorrowings).isEqualTo(1);
        verify(borrowingRepository, times(1)).updateById(updatedBorrowing.getId(), changedBook, borrowerId,
                borrowDate, expectedReturnDate, returned, returnedDate, lateReturnWeeklyFine, fineResolved);
    }

    @Test
    void updateById_borrowingNotFound_returnsZero() {
        Long nonExistingId = 11L;
        Long changedBook = 3L;
        when(borrowingRepository.updateById(nonExistingId, changedBook, null, null, null, null, null, null, null)).thenReturn(0);

        int numberOfUpdatedBorrowings = borrowingService.updateById(nonExistingId, changedBook, null, null,
                null, null, null, null, null);

        assertThat(numberOfUpdatedBorrowings).isEqualTo(0);
        verify(borrowingRepository, times(1)).updateById(nonExistingId, changedBook, null, null, null, null, null, null, null);
    }

    @Test
    void getFineById_borrowingNotFound_returnsEmptyOptional() {
        Long nonExistingId = 11L;
        when(borrowingRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Optional<BigDecimal> fine = borrowingService.getFineById(nonExistingId);

        assertThat(fine).isEmpty();
        verify(borrowingRepository, times(1)).findById(nonExistingId);
    }

    @Test
    void getFineById_bookReturnedFinePaid_returnsZeroFine() {
        Borrowing inActiveBorrowing = TestDataFactory.inActiveBorrowing;
        when(borrowingRepository.findById(inActiveBorrowing.getId())).thenReturn(Optional.of(inActiveBorrowing));

        Optional<BigDecimal> fine = borrowingService.getFineById(inActiveBorrowing.getId());

        assertThat(fine).isPresent();
        assertThat(fine.get()).isEqualTo(BigDecimal.ZERO);
        verify(borrowingRepository, times(1)).findById(inActiveBorrowing.getId());
    }

    @Test
    void getFineById_bookReturnedFineNotPaid_returnsSomeFine() {
        Borrowing inActiveBorrowing = TestDataFactory.inActiveBorrowingLate;
        when(borrowingRepository.findById(inActiveBorrowing.getId())).thenReturn(Optional.of(inActiveBorrowing));

        Optional<BigDecimal> fine = borrowingService.getFineById(inActiveBorrowing.getId());

        assertThat(fine).isPresent();
        assertThat(fine.get()).isEqualTo(new BigDecimal(12));
        verify(borrowingRepository, times(1)).findById(inActiveBorrowing.getId());
    }

    @Test
    void getFineById_bookNotReturnedStillHaveTime_returnsZeroFine() {
        Borrowing activeBorrowing = TestDataFactory.activeBorrowing;
        when(borrowingRepository.findById(activeBorrowing.getId())).thenReturn(Optional.of(activeBorrowing));

        Optional<BigDecimal> fine = borrowingService.getFineById(activeBorrowing.getId());

        assertThat(fine).isPresent();
        assertThat(fine.get()).isEqualTo(BigDecimal.ZERO);
        verify(borrowingRepository, times(1)).findById(activeBorrowing.getId());
    }

    @Test
    void getFineById_bookNotReturnedNowIsLate_returnsSomeFine() {
        Borrowing activeBorrowing = TestDataFactory.activeBorrowingLate;
        when(borrowingRepository.findById(activeBorrowing.getId())).thenReturn(Optional.of(activeBorrowing));

        try (MockedStatic<TimeProvider> timeProviderDummy = mockStatic(TimeProvider.class)) {
            timeProviderDummy.when(TimeProvider::now).thenReturn(activeBorrowing.getExpectedReturnDate().plusWeeks(5));

            Optional<BigDecimal> fine = borrowingService.getFineById(activeBorrowing.getId());

            assertThat(fine).isPresent();
            assertThat(fine.get()).isEqualTo(new BigDecimal(5));
            verify(borrowingRepository, times(1)).findById(activeBorrowing.getId());
        }
    }
}