package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.Rental;
import cz.muni.fi.pa165.data.repository.JpaRentalRepository;
import cz.muni.fi.pa165.repository.RentalRepository;
import cz.muni.fi.pa165.util.TestDataFactory;
import cz.muni.fi.pa165.util.TimeProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Sophia Zápotočná
 */
@ExtendWith(MockitoExtension.class)
class RentalServiceTest {
    @Mock
    private JpaRentalRepository jpaRentalRepository;

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private RentalService rentalService;

    @Test
    void findById_rentalFound_returnsRental() {
        Mockito.when(jpaRentalRepository.findById(1L)).thenReturn(Optional.ofNullable(TestDataFactory.activeRental));

        Optional<Rental> foundDao = rentalService.findById(1L);

        assertThat(foundDao).isPresent();
        assertThat(foundDao.get()).isEqualTo(TestDataFactory.activeRental);
    }

    @Test
    void findById_rentalNotFound_returnsEmptyOptional() {
        Mockito.when(jpaRentalRepository.findById(11L)).thenReturn(Optional.empty());

        Optional<Rental> foundDao = rentalService.findById(11L);

        assertThat(foundDao).isEmpty();
    }

    @Test
    void createRental_rentalCreated_returnsNewRental() {
        String book = "Rented book";
        String rentedBy = "User";
        OffsetDateTime borrowDate = OffsetDateTime
                .of(2024, 5, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime expectedReturnDate = borrowDate.plusWeeks(6);
        BigDecimal lateReturnWeeklyFine = new BigDecimal(100);
        Rental newRental = new Rental(book, rentedBy, borrowDate, expectedReturnDate, false,
                null, lateReturnWeeklyFine, false);
        Mockito.when(jpaRentalRepository.save(newRental)).thenReturn(newRental);

        try (MockedStatic<TimeProvider> timeProviderDummy = Mockito.mockStatic(TimeProvider.class)) {
            timeProviderDummy.when(TimeProvider::now).thenReturn(borrowDate);
            Rental rental = rentalService.createRental(book, rentedBy, expectedReturnDate, lateReturnWeeklyFine);

            assertThat(rental).isNotNull().isEqualTo(newRental);
            assertThat(rental.getBook()).isEqualTo(book);
            assertThat(rental.getRentedBy()).isEqualTo(rentedBy);
            assertThat(rental.getExpectedReturnDate()).isEqualTo(expectedReturnDate);
            assertThat(rental.getLateReturnWeeklyFine()).isEqualTo(lateReturnWeeklyFine);
            assertThat(rental.getBorrowDate()).isEqualTo(borrowDate);
            assertThat(rental.getReturnDate()).isNull();
            assertThat(rental.isReturned()).isFalse();
            assertThat(rental.isFineResolved()).isFalse();
            verify(jpaRentalRepository, times(1)).save(newRental);
        }
    }

    @Test
    void deleteById_rentalDeleted_callsRentalRepositoryOneTime() {
        Long idToDelete = 1L;

        rentalService.deleteById(idToDelete);

        verify(jpaRentalRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void findAll_rentalsReturned_returnsRentals() {
        List<Rental> rentals = new ArrayList<>();
        rentals.add(TestDataFactory.activeRental);
        rentals.add(TestDataFactory.inActiveRental);
        Mockito.when(jpaRentalRepository.findAll()).thenReturn(rentals);

        List<Rental> listOfRentals = rentalService.findAll();

        assertThat(listOfRentals)
                .isNotNull()
                .hasSize(2)
                .containsExactlyInAnyOrder(TestDataFactory.activeRental, TestDataFactory.inActiveRental);
    }

    @Test
    void updateById_oneItemChanged_returnsUpdatedRental() {
        String changedBook = "New changed book";
        Rental updatedRental = TestDataFactory.activeRental;
        Mockito.when(rentalRepository.findById(updatedRental.getId())).thenReturn(Optional.of(updatedRental));
        Mockito.when(rentalRepository.updateById(updatedRental.getId(), updatedRental))
                .thenReturn(Optional.ofNullable(TestDataFactory.activeRental));

        Optional<Rental> updatedDao = rentalService.updateById(updatedRental.getId(), changedBook, null, null,
                null, null, null, null, null);

        assertThat(updatedDao).isPresent();
        assertThat(updatedDao.get().getBook()).isEqualTo(changedBook);
        assertThat(updatedDao.get().getRentedBy()).isEqualTo(TestDataFactory.activeRental.getRentedBy());
        assertThat(updatedDao.get().getBorrowDate()).isEqualTo(TestDataFactory.activeRental.getBorrowDate());
        assertThat(updatedDao.get().getReturnDate()).isEqualTo(TestDataFactory.activeRental.getReturnDate());
        assertThat(updatedDao.get().getExpectedReturnDate())
                .isEqualTo(TestDataFactory.activeRental.getExpectedReturnDate());
        assertThat(updatedDao.get().getLateReturnWeeklyFine())
                .isEqualTo(TestDataFactory.activeRental.getLateReturnWeeklyFine());
        assertThat(updatedDao.get().isReturned()).isEqualTo(TestDataFactory.activeRental.isReturned());
        assertThat(updatedDao.get().isFineResolved()).isEqualTo(TestDataFactory.activeRental.isFineResolved());

        verify(rentalRepository, times(1)).findById(updatedRental.getId());
        verify(rentalRepository, times(1)).updateById(updatedRental.getId(), updatedRental);
    }

    @Test
    void updateById_someItemChanged_returnsUpdatedRental() {
        String changedBook = "New changed book";
        Boolean returned = true;
        OffsetDateTime returnDate = TimeProvider.now();
        Rental updatedRental = TestDataFactory.activeRental;
        Mockito.when(rentalRepository.findById(updatedRental.getId())).thenReturn(Optional.of(updatedRental));
        Mockito.when(rentalRepository.updateById(updatedRental.getId(), updatedRental))
                .thenReturn(Optional.ofNullable(TestDataFactory.activeRental));

        Optional<Rental> updatedDao = rentalService.updateById(updatedRental.getId(), changedBook, null, null,
                null, returned, returnDate, null, null);

        assertThat(updatedDao).isPresent();
        assertThat(updatedDao.get().getBook()).isEqualTo(changedBook);
        assertThat(updatedDao.get().getRentedBy()).isEqualTo(TestDataFactory.activeRental.getRentedBy());
        assertThat(updatedDao.get().getBorrowDate()).isEqualTo(TestDataFactory.activeRental.getBorrowDate());
        assertThat(updatedDao.get().getReturnDate()).isEqualTo(returnDate);
        assertThat(updatedDao.get().getExpectedReturnDate())
                .isEqualTo(TestDataFactory.activeRental.getExpectedReturnDate());
        assertThat(updatedDao.get().getLateReturnWeeklyFine())
                .isEqualTo(TestDataFactory.activeRental.getLateReturnWeeklyFine());
        assertThat(updatedDao.get().isReturned()).isEqualTo(returned);
        assertThat(updatedDao.get().isFineResolved()).isEqualTo(TestDataFactory.activeRental.isFineResolved());

        verify(rentalRepository, times(1)).findById(updatedRental.getId());
        verify(rentalRepository, times(1)).updateById(updatedRental.getId(), updatedRental);
    }

    @Test
    void updateById_allItemChanged_returnsUpdatedRental() {
        String changedBook = "New changed book";
        String rentedBy = "Changed user";
        Boolean returned = true;
        OffsetDateTime returnedDate = TimeProvider.now();
        OffsetDateTime expectedReturnDate = TimeProvider.now();
        OffsetDateTime borrowDate = TimeProvider.now().minusMonths(1L);
        Boolean fineResolved = true;
        BigDecimal lateReturnWeeklyFine = new BigDecimal(2);

        Rental updatedRental = TestDataFactory.activeRental;
        Mockito.when(rentalRepository.findById(updatedRental.getId())).thenReturn(Optional.of(updatedRental));
        Mockito.when(rentalRepository.updateById(updatedRental.getId(), updatedRental))
                .thenReturn(Optional.ofNullable(TestDataFactory.activeRental));

        Optional<Rental> updatedDao = rentalService.updateById(updatedRental.getId(), changedBook, rentedBy,
                borrowDate, expectedReturnDate, returned, returnedDate, lateReturnWeeklyFine, fineResolved);

        assertThat(updatedDao).isPresent();
        assertThat(updatedDao.get().getBook()).isEqualTo(changedBook);
        assertThat(updatedDao.get().getRentedBy()).isEqualTo(rentedBy);
        assertThat(updatedDao.get().getBorrowDate()).isEqualTo(borrowDate);
        assertThat(updatedDao.get().getReturnDate()).isEqualTo(returnedDate);
        assertThat(updatedDao.get().getExpectedReturnDate()).isEqualTo(expectedReturnDate);
        assertThat(updatedDao.get().getLateReturnWeeklyFine()).isEqualTo(lateReturnWeeklyFine);
        assertThat(updatedDao.get().isReturned()).isEqualTo(returned);
        assertThat(updatedDao.get().isFineResolved()).isEqualTo(fineResolved);

        verify(rentalRepository, times(1)).findById(updatedRental.getId());
        verify(rentalRepository, times(1)).updateById(updatedRental.getId(), updatedRental);
    }

    @Test
    void updateById_rentalNotFound_returnsEmptyOptional() {
        Long nonExistingId = 11L;
        String changedBook = "New changed book";
        Rental updatedRental = TestDataFactory.activeRental;
        Mockito.when(rentalRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Optional<Rental> updatedDao = rentalService.updateById(nonExistingId, changedBook, null, null,
                null, null, null, null, null);

        assertThat(updatedDao).isEmpty();

        verify(rentalRepository, times(1)).findById(nonExistingId);
        verify(rentalRepository, times(0)).updateById(nonExistingId, updatedRental);
    }

    @Test
    void getFineById_rentalNotFound_returnsEmptyOptional() {
        Long nonExistingId = 11L;
        Mockito.when(jpaRentalRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Optional<BigDecimal> fine = rentalService.getFineById(nonExistingId);

        assertThat(fine).isEmpty();
        verify(jpaRentalRepository, times(1)).findById(nonExistingId);
    }

    @Test
    void getFineById_bookReturnedFinePaid_returnsZeroFine() {
        Rental inActiveRental = TestDataFactory.inActiveRental;
        Mockito.when(jpaRentalRepository.findById(inActiveRental.getId())).thenReturn(Optional.of(inActiveRental));

        Optional<BigDecimal> fine = rentalService.getFineById(inActiveRental.getId());

        assertThat(fine).isPresent();
        assertThat(fine.get()).isEqualTo(BigDecimal.ZERO);
        verify(jpaRentalRepository, times(1)).findById(inActiveRental.getId());
    }

    @Test
    void getFineById_bookReturnedFineNotPaid_returnsSomeFine() {
        Rental inActiveRental = TestDataFactory.inActiveRentalLateDAO;
        Mockito.when(jpaRentalRepository.findById(inActiveRental.getId())).thenReturn(Optional.of(inActiveRental));

        Optional<BigDecimal> fine = rentalService.getFineById(inActiveRental.getId());

        assertThat(fine).isPresent();
        assertThat(fine.get()).isEqualTo(new BigDecimal(12));
        verify(jpaRentalRepository, times(1)).findById(inActiveRental.getId());
    }

    @Test
    void getFineById_bookNotReturnedStillHaveTime_returnsZeroFine() {
        Rental activeRental = TestDataFactory.activeRental;
        Mockito.when(jpaRentalRepository.findById(activeRental.getId())).thenReturn(Optional.of(activeRental));

        Optional<BigDecimal> fine = rentalService.getFineById(activeRental.getId());

        assertThat(fine).isPresent();
        assertThat(fine.get()).isEqualTo(BigDecimal.ZERO);
        verify(jpaRentalRepository, times(1)).findById(activeRental.getId());
    }

    @Test
    void getFineById_bookNotReturnedNowIsLate_returnsSomeFine() {
        Rental activeRental = TestDataFactory.activeRentalLateDAO;
        Mockito.when(jpaRentalRepository.findById(activeRental.getId())).thenReturn(Optional.of(activeRental));

        try (MockedStatic<TimeProvider> dummy = Mockito.mockStatic(TimeProvider.class)) {
            dummy.when(TimeProvider::now).thenReturn(activeRental.getExpectedReturnDate().plusWeeks(5));

            Optional<BigDecimal> fine = rentalService.getFineById(activeRental.getId());

            assertThat(fine).isPresent();
            assertThat(fine.get()).isEqualTo(new BigDecimal(5));
            verify(jpaRentalRepository, times(1)).findById(activeRental.getId());
        }
    }
}