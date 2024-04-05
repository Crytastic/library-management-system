package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.dao.ReservationDAO;
import cz.muni.fi.pa165.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
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

    private ReservationDAO reservationDAO;
    private List<ReservationDAO> reservationDAOList;

    @BeforeEach
    void setUp() {
        reservationDAO = new ReservationDAO("The Lord of the Rings", "Franta Vopršálek", OffsetDateTime.now(), OffsetDateTime.now().plusDays(3));
        reservationDAOList = Arrays.asList(
                new ReservationDAO("Active Book 1", "User 1", OffsetDateTime.now(), OffsetDateTime.now().plusDays(3)),
                new ReservationDAO("Active Book 2", "User 2", OffsetDateTime.now(), OffsetDateTime.now().plusDays(5))
        );
    }

    @Test
    void findById_existingId_callsReservationRepositoryFindById() {
        // Arrange
        Long id = 1L;
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservationDAO));

        // Act
        Optional<ReservationDAO> result = reservationService.findById(id);

        // Assert
        assertThat(result).isPresent().contains(reservationDAO);
        verify(reservationRepository, times(1)).findById(id);
    }

    @Test
    void findAll_validReservations_callsReservationRepositoryFindAll() {
        // Arrange
        when(reservationRepository.findAll()).thenReturn(reservationDAOList);

        // Act
        List<ReservationDAO> result = reservationService.findAll();

        // Assert
        assertThat(result).isEqualTo(reservationDAOList);
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void createReservation_validReservation_callsReservationRepositoryStore() {
        // Arrange
        when(reservationRepository.store(any(ReservationDAO.class))).thenReturn(reservationDAO);

        // Act
        ReservationDAO result = reservationService.createReservation("The Lord of the Rings", "Franta Vopršálek");

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
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservationDAO));
        when(reservationRepository.updateById(eq(id), any(ReservationDAO.class))).thenReturn(Optional.of(reservationDAO));

        // Act
        Optional<ReservationDAO> result = reservationService.updateById(id, newBook, newReservedBy, newReservedFrom, newReservedTo);

        // Assert
        assertThat(result).isPresent().contains(reservationDAO);
        assertThat(reservationDAO.getBook()).isEqualTo(newBook);
        assertThat(reservationDAO.getReservedBy()).isEqualTo(newReservedBy);
        assertThat(reservationDAO.getReservedFrom()).isEqualTo(newReservedFrom);
        assertThat(reservationDAO.getReservedTo()).isEqualTo(newReservedTo);
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

    @Test
    void findAllActive_callsReservationRepositoryFindAllActive() {
        // Arrange
        List<ReservationDAO> activeReservations = Arrays.asList(
                new ReservationDAO("Active Book 1", "User 1", OffsetDateTime.now(), OffsetDateTime.now().plusDays(3)),
                new ReservationDAO("Active Book 2", "User 2", OffsetDateTime.now(), OffsetDateTime.now().plusDays(5))
        );
        when(reservationRepository.findAllActive()).thenReturn(activeReservations);

        // Act
        List<ReservationDAO> result = reservationService.findAllActive();

        // Assert
        assertThat(result).isEqualTo(activeReservations);
        verify(reservationRepository, times(1)).findAllActive();
    }

    @Test
    void findAllExpired_callsReservationRepositoryFindAllExpired() {
        // Arrange
        List<ReservationDAO> expiredReservations = Arrays.asList(
                new ReservationDAO("Expired Book 1", "User 1", OffsetDateTime.now().minusDays(5), OffsetDateTime.now().minusDays(2)),
                new ReservationDAO("Expired Book 2", "User 2", OffsetDateTime.now().minusDays(3), OffsetDateTime.now().minusDays(1))
        );
        when(reservationRepository.findAllExpired()).thenReturn(expiredReservations);

        // Act
        List<ReservationDAO> result = reservationService.findAllExpired();

        // Assert
        assertThat(result).isEqualTo(expiredReservations);
        verify(reservationRepository, times(1)).findAllExpired();
    }
}
