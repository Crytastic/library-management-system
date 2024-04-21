package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.RentalFacade;
import cz.muni.fi.pa165.util.TimeProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.RentalDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
class RentalControllerTest {
    @Mock
    RentalFacade rentalFacade;

    @InjectMocks
    RentalController rentalController;

    @Test
    void findById_rentalFound_returnsRentalAndOkStatus() {
        RentalDTO rental = createDTORental();

        when(rentalFacade.findById(1L)).thenReturn(Optional.of(rental));

        ResponseEntity<RentalDTO> response = rentalController.getRental(1L);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(rental);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void findById_rentalNotFound_returnsNotFoundStatus() {
        when(rentalFacade.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<RentalDTO> response = rentalController.getRental(1L);

        assertThat(response.getBody()).isNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void createRental_rentalCreated_returnsNewRentalAndCreatedStatus() {
        String book = "Rented book";
        String rentedBy = "User";
        OffsetDateTime expectedReturnDate = OffsetDateTime
                .of(2024, 5, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        BigDecimal lateReturnWeeklyFine = new BigDecimal(100);
        RentalDTO rental = new RentalDTO().book(book).rentedBy(rentedBy).expectedReturnDate(expectedReturnDate)
                .lateReturnWeeklyFine(lateReturnWeeklyFine);

        when(rentalFacade.createRental(book, rentedBy, expectedReturnDate, lateReturnWeeklyFine)).thenReturn(rental);

        ResponseEntity<RentalDTO> response = rentalController
                .createRental(book, rentedBy, expectedReturnDate, lateReturnWeeklyFine);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(rental);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void getRentals_validOperation_returnsRentalsAndOkStatus() {
        RentalDTO rentalDTO = createDTORental();
        List<RentalDTO> rentals = new ArrayList<>();
        rentals.add(rentalDTO);
        when(rentalFacade.findAll()).thenReturn(rentals);

        ResponseEntity<List<RentalDTO>> response = rentalController.getRentals();

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(rentals);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void deleteById_rentalDelete_callsRentalsFacadeDelete() {
        Long idToDelete = 1L;

        rentalController.deleteRental(idToDelete);

        verify(rentalFacade, times(1)).deleteById(idToDelete);
    }

    @Test
    void updateRental_rentalFound_returnsOkStatus() {
        RentalDTO rentalDTO = createDTORental();
        when(rentalFacade.updateById(1L,
                rentalDTO.getBook(),
                rentalDTO.getRentedBy(),
                rentalDTO.getBorrowDate(),
                rentalDTO.getExpectedReturnDate(),
                rentalDTO.getReturned(),
                rentalDTO.getReturnDate(),
                rentalDTO.getLateReturnWeeklyFine(),
                rentalDTO.getFineResolved())).thenReturn(1);

        ResponseEntity<Void> response = rentalController.updateRental(1L,
                rentalDTO.getBook(),
                rentalDTO.getRentedBy(),
                rentalDTO.getBorrowDate(),
                rentalDTO.getExpectedReturnDate(),
                rentalDTO.getReturned(),
                rentalDTO.getReturnDate(),
                rentalDTO.getLateReturnWeeklyFine(),
                rentalDTO.getFineResolved());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void updateRental_rentalNotFound_returnsNotFoundStatus() {
        RentalDTO rentalDTO = createDTORental();
        when(rentalFacade.updateById(1L,
                rentalDTO.getBook(),
                rentalDTO.getRentedBy(),
                rentalDTO.getBorrowDate(),
                rentalDTO.getExpectedReturnDate(),
                rentalDTO.getReturned(),
                rentalDTO.getReturnDate(),
                rentalDTO.getLateReturnWeeklyFine(),
                rentalDTO.getFineResolved())).thenReturn(0);

        ResponseEntity<Void> response = rentalController.updateRental(1L,
                rentalDTO.getBook(),
                rentalDTO.getRentedBy(),
                rentalDTO.getBorrowDate(),
                rentalDTO.getExpectedReturnDate(),
                rentalDTO.getReturned(),
                rentalDTO.getReturnDate(),
                rentalDTO.getLateReturnWeeklyFine(),
                rentalDTO.getFineResolved());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getFineById_rentalFound_returnsFineAndOkStatus() {
        when(rentalFacade.getFineById(1L)).thenReturn(Optional.ofNullable(BigDecimal.TWO));

        ResponseEntity<BigDecimal> response = rentalController.getFineById(1L);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(BigDecimal.TWO);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getFineById_rentalNotFound_returnsNotFoundStatus() {
        when(rentalFacade.getFineById(1L)).thenReturn(Optional.empty());

        ResponseEntity<BigDecimal> response = rentalController.getFineById(1L);

        assertThat(response.getBody()).isNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }


    private static RentalDTO createDTORental() {
        return new RentalDTO()
                .book("Book")
                .rentedBy("Some user")
                .borrowDate(TimeProvider.now())
                .expectedReturnDate(TimeProvider.now().plusMonths(1))
                .returned(false)
                .returnDate(null)
                .lateReturnWeeklyFine(BigDecimal.TWO)
                .fineResolved(false);
    }

}