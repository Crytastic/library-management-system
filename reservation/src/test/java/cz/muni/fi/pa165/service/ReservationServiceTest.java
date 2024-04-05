package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.dao.ReservationDAO;
import cz.muni.fi.pa165.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;


    @Test
    void findById_existingId_callsReservationRepositoryFindById() {
        // Arrange
        Long id = 1L;
        ReservationDAO foundReservation = new ReservationDAO("The Lord of the Rings", "Franta Vopršálek", OffsetDateTime.now(), OffsetDateTime.now().plusDays(3));
        when(reservationRepository.findById(id)).thenReturn(Optional.of(foundReservation));

        // Act
        Optional<ReservationDAO> result = reservationService.findById(id);

        // Assert
        assertThat(result).isPresent().contains(foundReservation);
        verify(reservationRepository, times(1)).findById(id);
    }

    @Test
    void findAll_validReservations_callsReservationRepositoryFindAll() {
        // Arrange
        List<ReservationDAO> expectedReservations = Arrays.asList(
                new ReservationDAO("Book 1", "User 1", OffsetDateTime.now(), OffsetDateTime.now().plusDays(3)),
                new ReservationDAO("Book 2", "User 2", OffsetDateTime.now(), OffsetDateTime.now().plusDays(5))
        );
        when(reservationRepository.findAll()).thenReturn(expectedReservations);

        // Act
        List<ReservationDAO> result = reservationService.findAll();

        // Assert
        assertThat(result).isEqualTo(expectedReservations);
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void createReservation_validReservation_callsReservationRepositoryStore() {
        // Arrange
        String book = "The Hobbit";
        String reservedBy = "Bilbo Baggins";
        ReservationDAO reservationDAO = new ReservationDAO(book, reservedBy, OffsetDateTime.now(), OffsetDateTime.now().plusDays(3));
        when(reservationRepository.store(any(ReservationDAO.class))).thenReturn(reservationDAO);

        // Act
        ReservationDAO result = reservationService.createReservation(book, reservedBy);

        // Assert
        assertThat(result).isEqualTo(reservationDAO);
        verify(reservationRepository, times(1)).store(any(ReservationDAO.class));
    }

    @Test
    void updateById_existingId_callsReservationRepositoryUpdateById() {
        // Arrange
        Long id = 1L;
        String newBook = "Updated Book";
        String newReservedBy = "Updated User";
        OffsetDateTime newReservedFrom = OffsetDateTime.now().plusDays(1);
        OffsetDateTime newReservedTo = OffsetDateTime.now().plusDays(4);
        ReservationDAO originalReservation = new ReservationDAO("Original Book", "Original User", OffsetDateTime.now(), OffsetDateTime.now().plusDays(3));
        when(reservationRepository.findById(id)).thenReturn(Optional.of(originalReservation));
        when(reservationRepository.updateById(eq(id), any(ReservationDAO.class))).thenReturn(Optional.of(originalReservation));

        // Act
        Optional<ReservationDAO> result = reservationService.updateById(id, newBook, newReservedBy, newReservedFrom, newReservedTo);

        // Assert
        assertThat(result).isPresent().contains(originalReservation);
        assertThat(originalReservation.getBook()).isEqualTo(newBook);
        assertThat(originalReservation.getReservedBy()).isEqualTo(newReservedBy);
        assertThat(originalReservation.getReservedFrom()).isEqualTo(newReservedFrom);
        assertThat(originalReservation.getReservedTo()).isEqualTo(newReservedTo);
        verify(reservationRepository, times(1)).updateById(eq(id), any(ReservationDAO.class));
    }

    @Test
    void updateById_nonExistingId_returnsEmptyOptional() {
        // Arrange
        Long id = 1L;
        when(reservationRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<ReservationDAO> result = reservationService.updateById(id, "New Book", "New User", OffsetDateTime.now(), OffsetDateTime.now().plusDays(3));

        // Assert
        assertThat(result).isEmpty();
        verify(reservationRepository, never()).updateById(anyLong(), any(ReservationDAO.class));
    }

    @Test
    void deleteById_validId_callsReservationRepositoryDeleteById() {
        // Arrange
        Long id = 1L;

        // Act
        reservationService.deleteById(id);

        // Assert
        verify(reservationRepository, times(1)).deleteById(id);
    }
}
