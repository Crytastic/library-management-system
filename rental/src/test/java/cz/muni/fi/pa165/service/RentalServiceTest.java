package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.RentalDAO;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Sophia Zápotočná
 */
@ExtendWith(MockitoExtension.class)
class RentalServiceTest {
    @Mock
    private TimeProvider timeProvider;

    @Mock
    private RentalRepository rentalRepository;

    @InjectMocks
    private RentalService rentalService;

    @Test
    void findById_rentalFound_returnsRental() {
        Mockito.when(rentalRepository.findById(1L)).thenReturn(Optional.ofNullable(TestDataFactory.activeRentalDAO));

        Optional<RentalDAO> foundDao = rentalService.findById(1L);

        assertThat(foundDao).isPresent();
        assertThat(foundDao.get()).isEqualTo(TestDataFactory.activeRentalDAO);
    }

    @Test
    void findById_rentalNotFound_returnsEmptyOptional() {
        Mockito.when(rentalRepository.findById(11L)).thenReturn(Optional.empty());

        Optional<RentalDAO> foundDao = rentalService.findById(11L);

        assertThat(foundDao).isEmpty();
    }

    @Test
    void createRental_rentalCreated_returnsNewRental() {
        String book = "Rented book";
        String rentedBy = "User";
        OffsetDateTime expectedReturnDate = OffsetDateTime
                .of(2024, 5, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        BigDecimal lateReturnWeeklyFine = new BigDecimal(100);
        RentalDAO newRental = new RentalDAO(book, rentedBy, null, expectedReturnDate, false,
                null, lateReturnWeeklyFine, false);
        Mockito.when(rentalRepository.store(any(RentalDAO.class)))
                .thenReturn(newRental);

        RentalDAO rentalDAO = rentalService.createRental(book, rentedBy, expectedReturnDate, lateReturnWeeklyFine);

        assertThat(rentalDAO).isNotNull().isEqualTo(newRental);
        assertThat(rentalDAO.getBook()).isEqualTo(book);
        assertThat(rentalDAO.getRentedBy()).isEqualTo(rentedBy);
        assertThat(rentalDAO.getExpectedReturnDate()).isEqualTo(expectedReturnDate);
        assertThat(rentalDAO.getLateReturnWeeklyFine()).isEqualTo(lateReturnWeeklyFine);
        assertThat(rentalDAO.getBorrowDate()).isNull();
        assertThat(rentalDAO.getReturnDate()).isNull();
        assertThat(rentalDAO.isReturned()).isFalse();
        assertThat(rentalDAO.isFineResolved()).isFalse();
        verify(rentalRepository, times(1)).store(any(RentalDAO.class));
    }

    @Test
    void deleteById_rentalDeleted_callsRentalRepositoryOneTime() {
        Long idToDelete = 1L;

        rentalService.deleteById(idToDelete);

        verify(rentalRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void findAll_rentalsReturned_returnsRentals() {
        List<RentalDAO> rentals = new ArrayList<>();
        rentals.add(TestDataFactory.activeRentalDAO);
        rentals.add(TestDataFactory.inActiveRentalDAO);
        Mockito.when(rentalRepository.findAll()).thenReturn(rentals);

        List<RentalDAO> listOfRentals = rentalService.findAll();

        assertThat(listOfRentals)
                .isNotNull()
                .hasSize(2)
                .containsExactlyInAnyOrder(TestDataFactory.activeRentalDAO, TestDataFactory.inActiveRentalDAO);
    }

    @Test
    void updateById_oneItemChanged_returnsUpdatedRental() {
        String changedBook = "New changed book";
        RentalDAO updatedRental = TestDataFactory.activeRentalDAO;
        Mockito.when(rentalRepository.findById(updatedRental.getId())).thenReturn(Optional.of(updatedRental));
        Mockito.when(rentalRepository.updateById(updatedRental.getId(), updatedRental))
                .thenReturn(Optional.ofNullable(TestDataFactory.activeRentalDAO));

        Optional<RentalDAO> updatedDao = rentalService.updateById(updatedRental.getId(), changedBook, null, null,
                null, null, null, null, null);

        assertThat(updatedDao).isPresent();
        assertThat(updatedDao.get().getBook()).isEqualTo(changedBook);
        assertThat(updatedDao.get().getRentedBy()).isEqualTo(TestDataFactory.activeRentalDAO.getRentedBy());
        assertThat(updatedDao.get().getBorrowDate()).isEqualTo(TestDataFactory.activeRentalDAO.getBorrowDate());
        assertThat(updatedDao.get().getReturnDate()).isEqualTo(TestDataFactory.activeRentalDAO.getReturnDate());
        assertThat(updatedDao.get().getExpectedReturnDate())
                .isEqualTo(TestDataFactory.activeRentalDAO.getExpectedReturnDate());
        assertThat(updatedDao.get().getLateReturnWeeklyFine())
                .isEqualTo(TestDataFactory.activeRentalDAO.getLateReturnWeeklyFine());
        assertThat(updatedDao.get().isReturned()).isEqualTo(TestDataFactory.activeRentalDAO.isReturned());
        assertThat(updatedDao.get().isFineResolved()).isEqualTo(TestDataFactory.activeRentalDAO.isFineResolved());

        verify(rentalRepository, times(1)).findById(updatedRental.getId());
        verify(rentalRepository, times(1)).updateById(updatedRental.getId(), updatedRental);
    }

    @Test
    void updateById_someItemChanged_returnsUpdatedRental() {
        String changedBook = "New changed book";
        Boolean returned = true;
        OffsetDateTime returnDate = TimeProvider.now();
        RentalDAO updatedRental = TestDataFactory.activeRentalDAO;
        Mockito.when(rentalRepository.findById(updatedRental.getId())).thenReturn(Optional.of(updatedRental));
        Mockito.when(rentalRepository.updateById(updatedRental.getId(), updatedRental))
                .thenReturn(Optional.ofNullable(TestDataFactory.activeRentalDAO));

        Optional<RentalDAO> updatedDao = rentalService.updateById(updatedRental.getId(), changedBook, null, null,
                null, returned, returnDate, null, null);

        assertThat(updatedDao).isPresent();
        assertThat(updatedDao.get().getBook()).isEqualTo(changedBook);
        assertThat(updatedDao.get().getRentedBy()).isEqualTo(TestDataFactory.activeRentalDAO.getRentedBy());
        assertThat(updatedDao.get().getBorrowDate()).isEqualTo(TestDataFactory.activeRentalDAO.getBorrowDate());
        assertThat(updatedDao.get().getReturnDate()).isEqualTo(returnDate);
        assertThat(updatedDao.get().getExpectedReturnDate())
                .isEqualTo(TestDataFactory.activeRentalDAO.getExpectedReturnDate());
        assertThat(updatedDao.get().getLateReturnWeeklyFine())
                .isEqualTo(TestDataFactory.activeRentalDAO.getLateReturnWeeklyFine());
        assertThat(updatedDao.get().isReturned()).isEqualTo(returned);
        assertThat(updatedDao.get().isFineResolved()).isEqualTo(TestDataFactory.activeRentalDAO.isFineResolved());

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

        RentalDAO updatedRental = TestDataFactory.activeRentalDAO;
        Mockito.when(rentalRepository.findById(updatedRental.getId())).thenReturn(Optional.of(updatedRental));
        Mockito.when(rentalRepository.updateById(updatedRental.getId(), updatedRental))
                .thenReturn(Optional.ofNullable(TestDataFactory.activeRentalDAO));

        Optional<RentalDAO> updatedDao = rentalService.updateById(updatedRental.getId(), changedBook, rentedBy,
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
        RentalDAO updatedRental = TestDataFactory.activeRentalDAO;
        Mockito.when(rentalRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Optional<RentalDAO> updatedDao = rentalService.updateById(nonExistingId, changedBook, null, null,
                null, null, null, null, null);

        assertThat(updatedDao).isEmpty();

        verify(rentalRepository, times(1)).findById(nonExistingId);
        verify(rentalRepository, times(0)).updateById(nonExistingId, updatedRental);
    }

    @Test
    void getFineById_rentalNotFound_returnsEmptyOptional() {
        Long nonExistingId = 11L;
        Mockito.when(rentalRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Optional<BigDecimal> fine = rentalService.getFineById(nonExistingId);

        assertThat(fine).isEmpty();
        verify(rentalRepository, times(1)).findById(nonExistingId);
    }

    @Test
    void getFineById_bookReturnedFinePaid_returnsZeroFine() {
        RentalDAO inActiveRental = TestDataFactory.inActiveRentalDAO;
        Mockito.when(rentalRepository.findById(inActiveRental.getId())).thenReturn(Optional.of(inActiveRental));

        Optional<BigDecimal> fine = rentalService.getFineById(inActiveRental.getId());

        assertThat(fine).isPresent();
        assertThat(fine.get()).isEqualTo(BigDecimal.ZERO);
        verify(rentalRepository, times(1)).findById(inActiveRental.getId());
    }

    @Test
    void getFineById_bookReturnedFineNotPaid_returnsSomeFine() {
        RentalDAO inActiveRental = TestDataFactory.inActiveRentalLateDAO;
        Mockito.when(rentalRepository.findById(inActiveRental.getId())).thenReturn(Optional.of(inActiveRental));

        Optional<BigDecimal> fine = rentalService.getFineById(inActiveRental.getId());

        assertThat(fine).isPresent();
        assertThat(fine.get()).isEqualTo(new BigDecimal(12));
        verify(rentalRepository, times(1)).findById(inActiveRental.getId());
    }

    @Test
    void getFineById_bookNotReturnedStillHaveTime_returnsZeroFine() {
        RentalDAO activeRental = TestDataFactory.activeRentalDAO;
        Mockito.when(rentalRepository.findById(activeRental.getId())).thenReturn(Optional.of(activeRental));

        Optional<BigDecimal> fine = rentalService.getFineById(activeRental.getId());

        assertThat(fine).isPresent();
        assertThat(fine.get()).isEqualTo(BigDecimal.ZERO);
        verify(rentalRepository, times(1)).findById(activeRental.getId());
    }

    @Test
    void getFineById_bookNotReturnedNowIsLate_returnsSomeFine() {
        RentalDAO activeRental = TestDataFactory.activeRentalLateDAO;
        Mockito.when(rentalRepository.findById(activeRental.getId())).thenReturn(Optional.of(activeRental));

        try (MockedStatic<TimeProvider> dummy = Mockito.mockStatic(TimeProvider.class)) {
            dummy.when(TimeProvider::now).thenReturn(activeRental.getExpectedReturnDate().plusWeeks(5));

            Optional<BigDecimal> fine = rentalService.getFineById(activeRental.getId());

            assertThat(fine).isPresent();
            assertThat(fine.get()).isEqualTo(new BigDecimal(5));
            verify(rentalRepository, times(1)).findById(activeRental.getId());
        }
    }
}