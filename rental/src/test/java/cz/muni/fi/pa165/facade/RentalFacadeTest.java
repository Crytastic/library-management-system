package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.data.model.RentalDAO;
import cz.muni.fi.pa165.service.RentalService;
import cz.muni.fi.pa165.util.TestDataFactory;
import cz.muni.fi.pa165.util.TimeProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.RentalDTO;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Sophia Zápotočná
 */
@ExtendWith(MockitoExtension.class)
class RentalFacadeTest {
    @Mock
    RentalService rentalService;

    @InjectMocks
    RentalFacade rentalFacade;

    @Test
    void findById_rentalFound_returnsRental() {
        Mockito.when(rentalService.findById(2L)).thenReturn(Optional.ofNullable(TestDataFactory.activeRentalDAO));

        Optional<RentalDTO> rentalDTO = rentalFacade.findById(2L);

        assertThat(rentalDTO).isPresent();
        assertThat(rentalDTO.get()).isEqualTo(convertToDTO(TestDataFactory.activeRentalDAO));
    }

    @Test
    void findById_rentalNotFound_returnsRental() {
        Mockito.when(rentalService.findById(11L)).thenReturn(Optional.empty());

        Optional<RentalDTO> rentalDTO = rentalFacade.findById(11L);

        assertThat(rentalDTO).isEmpty();
    }

    @Test
    void createRental_rentalCreated_returnsNewRental() {
        String book = "Rented book";
        String rentedBy = "User";
        OffsetDateTime expectedReturnDate = OffsetDateTime
                .of(2024, 5, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        BigDecimal lateReturnWeeklyFine = new BigDecimal(100);
        RentalDAO newRentalDAO = new RentalDAO(book, rentedBy, null, expectedReturnDate, false,
                null, lateReturnWeeklyFine, false);

        Mockito.when(rentalService.createRental(
                        anyString(),
                        anyString(),
                        any(OffsetDateTime.class),
                        any(BigDecimal.class)))
                .thenReturn(newRentalDAO);

        RentalDTO rentalDTO = rentalFacade.createRental(book, rentedBy, expectedReturnDate, lateReturnWeeklyFine);

        assertThat(rentalDTO).isNotNull();
        assertThat(rentalDTO.getBook()).isEqualTo(book);
        assertThat(rentalDTO.getRentedBy()).isEqualTo(rentedBy);
        assertThat(rentalDTO.getExpectedReturnDate()).isEqualTo(expectedReturnDate);
        assertThat(rentalDTO.getLateReturnWeeklyFine()).isEqualTo(lateReturnWeeklyFine);
        assertThat(rentalDTO.getBorrowDate()).isNull();
        assertThat(rentalDTO.getReturnDate()).isNull();
        assertThat(rentalDTO.getReturned()).isFalse();
        assertThat(rentalDTO.getFineResolved()).isFalse();
        verify(rentalService, times(1)).createRental(
                anyString(),
                anyString(),
                any(OffsetDateTime.class),
                any(BigDecimal.class));
    }

    @Test
    void deleteById_rentalDeleted_callsRentalRepositoryOneTime() {
        Long idToDelete = 1L;

        rentalFacade.deleteById(idToDelete);

        verify(rentalService, times(1)).deleteById(idToDelete);
    }

    @Test
    void findAll_rentalsReturned_returnsRentals() {
        List<RentalDAO> rentals = new ArrayList<>();
        rentals.add(TestDataFactory.activeRentalDAO);
        rentals.add(TestDataFactory.inActiveRentalDAO);
        Mockito.when(rentalService.findAll()).thenReturn(rentals);

        List<RentalDTO> listOfRentals = rentalFacade.findAll();

        assertThat(listOfRentals)
                .isNotNull()
                .hasSize(2)
                .containsExactlyInAnyOrder(convertToDTO(TestDataFactory.activeRentalDAO),
                        convertToDTO(TestDataFactory.inActiveRentalDAO));
    }

    @Test
    void updateById_validParameters_callsUpdateByIdOnService() {
        RentalDAO updatedRental = new RentalDAO(
                "Inactive test book",
                "Rental creator",
                TimeProvider.now(),
                TimeProvider.now(),
                true,
                TimeProvider.now(),
                new BigDecimal(3),
                true);
        updatedRental.setId(3L);

        Mockito.when(rentalService.updateById(
                        updatedRental.getId(),
                        updatedRental.getBook(),
                        updatedRental.getRentedBy(),
                        updatedRental.getBorrowDate(),
                        updatedRental.getExpectedReturnDate(),
                        updatedRental.isReturned(),
                        updatedRental.getReturnDate(),
                        updatedRental.getLateReturnWeeklyFine(),
                        updatedRental.isFineResolved()))
                .thenReturn(Optional.of(updatedRental));

        Optional<RentalDTO> updatedDto = rentalFacade.updateById(
                updatedRental.getId(),
                updatedRental.getBook(),
                updatedRental.getRentedBy(),
                updatedRental.getBorrowDate(),
                updatedRental.getExpectedReturnDate(),
                updatedRental.isReturned(),
                updatedRental.getReturnDate(),
                updatedRental.getLateReturnWeeklyFine(),
                updatedRental.isFineResolved());


        assertThat(updatedDto).isPresent().contains(convertToDTO(updatedRental));

        verify(rentalService, times(1)).updateById(
                updatedRental.getId(),
                updatedRental.getBook(),
                updatedRental.getRentedBy(),
                updatedRental.getBorrowDate(),
                updatedRental.getExpectedReturnDate(),
                updatedRental.isReturned(),
                updatedRental.getReturnDate(),
                updatedRental.getLateReturnWeeklyFine(),
                updatedRental.isFineResolved());
    }

    @Test
    void getFineById_validRental_callsGetFineByIdOnService() {
        Mockito.when(rentalService.getFineById(1L)).thenReturn(Optional.of(BigDecimal.ZERO));

        Optional<BigDecimal> fine = rentalFacade.getFineById(1L);

        assertThat(fine).isPresent();
        assertThat(fine.get()).isEqualTo(BigDecimal.ZERO);
        verify(rentalService, times(1)).getFineById(1L);
    }

    private RentalDTO convertToDTO(RentalDAO rentalDAO) {
        return new RentalDTO()
                .id(rentalDAO.getId())
                .book(rentalDAO.getBook())
                .rentedBy(rentalDAO.getRentedBy())
                .borrowDate(rentalDAO.getBorrowDate())
                .expectedReturnDate(rentalDAO.getExpectedReturnDate())
                .returned(rentalDAO.isReturned())
                .returnDate(rentalDAO.getReturnDate())
                .lateReturnWeeklyFine(rentalDAO.getLateReturnWeeklyFine())
                .fineResolved(rentalDAO.isFineResolved());
    }

}