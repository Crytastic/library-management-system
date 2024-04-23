package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.Rental;
import cz.muni.fi.pa165.data.repository.RentalRepository;
import cz.muni.fi.pa165.util.TestDataFactory;
import cz.muni.fi.pa165.util.TimeProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
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
class RentalServiceTest {
    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    @Spy
    private RentalService rentalService;

    @Test
    void findById_rentalFound_returnsRental() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.ofNullable(TestDataFactory.activeRental));

        Optional<Rental> found = rentalService.findById(1L);

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(TestDataFactory.activeRental);
    }

    @Test
    void findById_rentalNotFound_returnsEmptyOptional() {
        when(rentalRepository.findById(11L)).thenReturn(Optional.empty());

        Optional<Rental> found = rentalService.findById(11L);

        assertThat(found).isEmpty();
    }

    @Test
    void createRental_rentalCreated_returnsNewRental() {
        Long bookId = 7L;
        Long borrowerId = 8L;
        OffsetDateTime borrowDate = OffsetDateTime
                .of(2024, 5, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = borrowDate.plusWeeks(6);
        BigDecimal lateReturnWeeklyFine = new BigDecimal(100);
        Rental newRental = new Rental(bookId, borrowerId, borrowDate, expectedReturnDate, false,
                null, lateReturnWeeklyFine, false);
        when(rentalRepository.save(newRental)).thenReturn(newRental);

        try (MockedStatic<TimeProvider> timeProviderDummy = mockStatic(TimeProvider.class)) {
            timeProviderDummy.when(TimeProvider::now).thenReturn(borrowDate);
            Rental rental = rentalService.createRental(bookId, borrowerId, expectedReturnDate, lateReturnWeeklyFine);

            assertThat(rental).isNotNull().isEqualTo(newRental);
            assertThat(rental.getBookId()).isEqualTo(bookId);
            assertThat(rental.getBorrowerId()).isEqualTo(borrowerId);
            assertThat(rental.getExpectedReturnDate()).isEqualTo(expectedReturnDate);
            assertThat(rental.getLateReturnWeeklyFine()).isEqualTo(lateReturnWeeklyFine);
            assertThat(rental.getBorrowDate()).isEqualTo(borrowDate);
            assertThat(rental.getReturnDate()).isNull();
            assertThat(rental.isReturned()).isFalse();
            assertThat(rental.isFineResolved()).isFalse();
            verify(rentalRepository, times(1)).save(newRental);
        }
    }

    @Test
    void deleteById_rentalDeleted_callsRentalRepositoryOneTime() {
        Long idToDelete = 1L;

        rentalService.deleteById(idToDelete);

        verify(rentalRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void findAll_rentalsReturned_returnsRentals() {
        List<Rental> rentals = new ArrayList<>();
        rentals.add(TestDataFactory.activeRental);
        rentals.add(TestDataFactory.inActiveRental);
        when(rentalRepository.findAll()).thenReturn(rentals);

        List<Rental> listOfRentals = rentalService.findAll();

        assertThat(listOfRentals)
                .isNotNull()
                .hasSize(2)
                .containsExactlyInAnyOrder(TestDataFactory.activeRental, TestDataFactory.inActiveRental);
    }

    @Test
    void returnBook_noFine_returnsOne() {
        // Arrange
        Rental rental = TestDataFactory.activeRental;
        OffsetDateTime returnDate = rental.getExpectedReturnDate().minusWeeks(2);
        when(rentalRepository.updateById(rental.getId(), null, null, null,
                null, true, returnDate, null, true))
                .thenReturn(1);
        when(rentalService.getFineById(2L)).thenReturn(Optional.of(BigDecimal.ZERO));

        try (MockedStatic<TimeProvider> timeProviderDummy = mockStatic(TimeProvider.class)) {
            timeProviderDummy.when(TimeProvider::now).thenReturn(returnDate);

            // Act
            int numberOfUpdatedRentals = rentalService.returnBook(rental.getId());

            // Assert
            assertThat(numberOfUpdatedRentals).isEqualTo(1);
            verify(rentalRepository, times(1)).updateById(rental.getId(), null, null, null,
                    null, true, returnDate, null, true);
        }
    }

    @Test
    void updateById_oneItemChanged_returnsOne() {
        Long changedBook = 2L;
        Rental updatedRental = TestDataFactory.activeRental;
        when(rentalRepository.updateById(updatedRental.getId(), changedBook, null, null,
                null, null, null, null, null))
                .thenReturn(1);

        int numberOfUpdatedRentals = rentalService.updateById(updatedRental.getId(), changedBook, null, null,
                null, null, null, null, null);

        assertThat(numberOfUpdatedRentals).isEqualTo(1);
        verify(rentalRepository, times(1)).updateById(updatedRental.getId(), changedBook, null, null,
                null, null, null, null, null);
    }

    @Test
    void updateById_someItemChanged_returnsOne() {
        Long changedBook = 3L;
        Boolean returned = true;
        OffsetDateTime returnDate = TimeProvider.now();
        Rental updatedRental = TestDataFactory.activeRental;
        when(rentalRepository.updateById(updatedRental.getId(), changedBook, null, null,
                null, returned, returnDate, null, null))
                .thenReturn(1);


        int numberOfUpdatedRentals = rentalRepository.updateById(updatedRental.getId(), changedBook, null, null,
                null, returned, returnDate, null, null);


        assertThat(numberOfUpdatedRentals).isEqualTo(1);
        verify(rentalRepository, times(1)).updateById(updatedRental.getId(), changedBook, null, null,
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
        Rental updatedRental = TestDataFactory.activeRental;
        when(rentalRepository.updateById(updatedRental.getId(), changedBook, borrowerId,
                borrowDate, expectedReturnDate, returned, returnedDate, lateReturnWeeklyFine, fineResolved)).thenReturn(1);

        int numberOfUpdatedRentals = rentalService.updateById(updatedRental.getId(), changedBook, borrowerId,
                borrowDate, expectedReturnDate, returned, returnedDate, lateReturnWeeklyFine, fineResolved);

        assertThat(numberOfUpdatedRentals).isEqualTo(1);
        verify(rentalRepository, times(1)).updateById(updatedRental.getId(), changedBook, borrowerId,
                borrowDate, expectedReturnDate, returned, returnedDate, lateReturnWeeklyFine, fineResolved);
    }

    @Test
    void updateById_rentalNotFound_returnsZero() {
        Long nonExistingId = 11L;
        Long changedBook = 3L;
        when(rentalRepository.updateById(nonExistingId, changedBook, null, null, null, null, null, null, null)).thenReturn(0);

        int numberOfUpdatedRentals = rentalService.updateById(nonExistingId, changedBook, null, null,
                null, null, null, null, null);

        assertThat(numberOfUpdatedRentals).isEqualTo(0);
        verify(rentalRepository, times(1)).updateById(nonExistingId, changedBook, null, null, null, null, null, null, null);
    }

    @Test
    void getFineById_rentalNotFound_returnsEmptyOptional() {
        Long nonExistingId = 11L;
        when(rentalRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Optional<BigDecimal> fine = rentalService.getFineById(nonExistingId);

        assertThat(fine).isEmpty();
        verify(rentalRepository, times(1)).findById(nonExistingId);
    }

    @Test
    void getFineById_bookReturnedFinePaid_returnsZeroFine() {
        Rental inActiveRental = TestDataFactory.inActiveRental;
        when(rentalRepository.findById(inActiveRental.getId())).thenReturn(Optional.of(inActiveRental));

        Optional<BigDecimal> fine = rentalService.getFineById(inActiveRental.getId());

        assertThat(fine).isPresent();
        assertThat(fine.get()).isEqualTo(BigDecimal.ZERO);
        verify(rentalRepository, times(1)).findById(inActiveRental.getId());
    }

    @Test
    void getFineById_bookReturnedFineNotPaid_returnsSomeFine() {
        Rental inActiveRental = TestDataFactory.inActiveRentalLate;
        when(rentalRepository.findById(inActiveRental.getId())).thenReturn(Optional.of(inActiveRental));

        Optional<BigDecimal> fine = rentalService.getFineById(inActiveRental.getId());

        assertThat(fine).isPresent();
        assertThat(fine.get()).isEqualTo(new BigDecimal(12));
        verify(rentalRepository, times(1)).findById(inActiveRental.getId());
    }

    @Test
    void getFineById_bookNotReturnedStillHaveTime_returnsZeroFine() {
        Rental activeRental = TestDataFactory.activeRental;
        when(rentalRepository.findById(activeRental.getId())).thenReturn(Optional.of(activeRental));

        Optional<BigDecimal> fine = rentalService.getFineById(activeRental.getId());

        assertThat(fine).isPresent();
        assertThat(fine.get()).isEqualTo(BigDecimal.ZERO);
        verify(rentalRepository, times(1)).findById(activeRental.getId());
    }

    @Test
    void getFineById_bookNotReturnedNowIsLate_returnsSomeFine() {
        Rental activeRental = TestDataFactory.activeRentalLate;
        when(rentalRepository.findById(activeRental.getId())).thenReturn(Optional.of(activeRental));

        try (MockedStatic<TimeProvider> timeProviderDummy = mockStatic(TimeProvider.class)) {
            timeProviderDummy.when(TimeProvider::now).thenReturn(activeRental.getExpectedReturnDate().plusWeeks(5));

            Optional<BigDecimal> fine = rentalService.getFineById(activeRental.getId());

            assertThat(fine).isPresent();
            assertThat(fine.get()).isEqualTo(new BigDecimal(5));
            verify(rentalRepository, times(1)).findById(activeRental.getId());
        }
    }
}