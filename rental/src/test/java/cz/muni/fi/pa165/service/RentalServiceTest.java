package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.dao.RentalDAO;
import cz.muni.fi.pa165.repository.RentalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {
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




}