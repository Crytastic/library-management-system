package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.dao.RentalDAO;
import cz.muni.fi.pa165.service.RentalService;
import cz.muni.fi.pa165.util.TestDataFactory;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        assertThat(rentalDTO.get()).isEqualTo(TestDataFactory.activeRentalDTO);
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

}