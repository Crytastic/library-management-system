package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.data.model.Rental;
import cz.muni.fi.pa165.mappers.RentalMapper;
import cz.muni.fi.pa165.service.RentalService;
import cz.muni.fi.pa165.util.TestDataFactory;
import cz.muni.fi.pa165.util.TimeProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.RentalDTO;

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
class RentalFacadeTest {
    @Mock
    RentalService rentalService;

    @Mock
    RentalMapper rentalMapper;

    @InjectMocks
    RentalFacade rentalFacade;

    @Test
    void findById_rentalFound_returnsRental() {
        when(rentalService.findById(2L)).thenReturn(Optional.ofNullable(TestDataFactory.activeRental));
        when(rentalMapper.mapToDto(TestDataFactory.activeRental)).thenReturn(TestDataFactory.activeRentalDTO);

        Optional<RentalDTO> rentalDTO = rentalFacade.findById(2L);

        assertThat(rentalDTO).isPresent();
        assertThat(rentalDTO.get()).isEqualTo(TestDataFactory.activeRentalDTO);
    }

    @Test
    void findById_rentalNotFound_returnsRental() {
        when(rentalService.findById(11L)).thenReturn(Optional.empty());

        Optional<RentalDTO> rentalDTO = rentalFacade.findById(11L);

        assertThat(rentalDTO).isEmpty();
    }

    @Test
    void createRental_rentalCreated_returnsNewRental() {
        Long bookId = 17L;
        Long borrowerId = 18L;
        OffsetDateTime expectedReturnDate = OffsetDateTime
                .of(2024, 5, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        BigDecimal lateReturnWeeklyFine = new BigDecimal(100);
        Rental newRental = new Rental(bookId, borrowerId, null, expectedReturnDate, false,
                null, lateReturnWeeklyFine, false);
        RentalDTO newRentalDTO = new RentalDTO()
                .bookId(bookId)
                .borrowerId(borrowerId)
                .borrowDate(null)
                .expectedReturnDate(expectedReturnDate)
                .returned(false)
                .lateReturnWeeklyFine(lateReturnWeeklyFine)
                .fineResolved(false);


        when(rentalService.createRental(
                bookId,
                borrowerId,
                expectedReturnDate,
                lateReturnWeeklyFine))
                .thenReturn(newRental);
        when(rentalMapper.mapToDto(newRental)).thenReturn(newRentalDTO);

        RentalDTO rentalDTO = rentalFacade.createRental(bookId, borrowerId, expectedReturnDate, lateReturnWeeklyFine);

        assertThat(rentalDTO).isNotNull();
        assertThat(rentalDTO.getBookId()).isEqualTo(bookId);
        assertThat(rentalDTO.getBorrowerId()).isEqualTo(borrowerId);
        assertThat(rentalDTO.getExpectedReturnDate()).isEqualTo(expectedReturnDate);
        assertThat(rentalDTO.getLateReturnWeeklyFine()).isEqualTo(lateReturnWeeklyFine);
        assertThat(rentalDTO.getBorrowDate()).isNull();
        assertThat(rentalDTO.getReturnDate()).isNull();
        assertThat(rentalDTO.getReturned()).isFalse();
        assertThat(rentalDTO.getFineResolved()).isFalse();
        verify(rentalService, times(1)).createRental(
                bookId,
                borrowerId,
                expectedReturnDate,
                lateReturnWeeklyFine);
    }

    @Test
    void deleteById_rentalDeleted_callsRentalRepositoryOneTime() {
        Long idToDelete = 1L;

        rentalFacade.deleteById(idToDelete);

        verify(rentalService, times(1)).deleteById(idToDelete);
    }

    @Test
    void findAll_rentalsReturned_returnsRentals() {
        List<Rental> rentals = new ArrayList<>();
        rentals.add(TestDataFactory.activeRental);
        rentals.add(TestDataFactory.inActiveRental);
        when(rentalService.findAll()).thenReturn(rentals);
        when(rentalMapper.mapToList(rentals)).thenReturn(List.of(TestDataFactory.activeRentalDTO, TestDataFactory.inAactiveRentalDTO));

        List<RentalDTO> listOfRentals = rentalFacade.findAll();

        assertThat(listOfRentals)
                .isNotNull()
                .hasSize(2)
                .containsExactlyInAnyOrder(TestDataFactory.activeRentalDTO, TestDataFactory.inAactiveRentalDTO);
    }

    @Test
    void updateById_validParameters_callsUpdateByIdOnService() {
        Rental updatedRental = new Rental(
                17L,
                18L,
                TimeProvider.now(),
                TimeProvider.now(),
                true,
                TimeProvider.now(),
                new BigDecimal(3),
                true);
        updatedRental.setId(3L);

        when(rentalService.updateById(
                updatedRental.getId(),
                updatedRental.getBookId(),
                updatedRental.getBorrowerId(),
                updatedRental.getBorrowDate(),
                updatedRental.getExpectedReturnDate(),
                updatedRental.isReturned(),
                updatedRental.getReturnDate(),
                updatedRental.getLateReturnWeeklyFine(),
                updatedRental.isFineResolved()))
                .thenReturn(1);

        int numberOfUpdatedRentals = rentalFacade.updateById(
                updatedRental.getId(),
                updatedRental.getBookId(),
                updatedRental.getBorrowerId(),
                updatedRental.getBorrowDate(),
                updatedRental.getExpectedReturnDate(),
                updatedRental.isReturned(),
                updatedRental.getReturnDate(),
                updatedRental.getLateReturnWeeklyFine(),
                updatedRental.isFineResolved());

        assertThat(numberOfUpdatedRentals).isEqualTo(1);
        verify(rentalService, times(1)).updateById(
                updatedRental.getId(),
                updatedRental.getBookId(),
                updatedRental.getBorrowerId(),
                updatedRental.getBorrowDate(),
                updatedRental.getExpectedReturnDate(),
                updatedRental.isReturned(),
                updatedRental.getReturnDate(),
                updatedRental.getLateReturnWeeklyFine(),
                updatedRental.isFineResolved());
    }

    @Test
    void getFineById_validRental_callsGetFineByIdOnService() {
        when(rentalService.getFineById(1L)).thenReturn(Optional.of(BigDecimal.ZERO));

        Optional<BigDecimal> fine = rentalFacade.getFineById(1L);

        assertThat(fine).isPresent();
        assertThat(fine.get()).isEqualTo(BigDecimal.ZERO);
        verify(rentalService, times(1)).getFineById(1L);
    }

}