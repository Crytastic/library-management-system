package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.Reservation;
import cz.muni.fi.pa165.data.repository.JpaReservationRepository;
import cz.muni.fi.pa165.repository.ReservationRepository;
import cz.muni.fi.pa165.util.TimeProvider;
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

/**
 * @author Maxmilián Šeffer
 */
@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private JpaReservationRepository jpaReservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation;
    private List<Reservation> reservationList;

    @BeforeEach
    void setUp() {
        reservation = new Reservation("The Lord of the Rings", "Franta Vopršálek", TimeProvider.now(), TimeProvider.now().plusDays(3));
        reservationList = Arrays.asList(
                new Reservation("Active Book 1", "User 1", TimeProvider.now(), TimeProvider.now().plusDays(3)),
                new Reservation("Active Book 2", "User 2", TimeProvider.now(), TimeProvider.now().plusDays(5))
        );
    }

    @Test
    void findById_existingId_callsReservationRepositoryFindById() {
        // Arrange
        Long id = 1L;
        when(jpaReservationRepository.findById(id)).thenReturn(Optional.of(reservation));

        // Act
        Optional<Reservation> result = reservationService.findById(id);

        // Assert
        assertThat(result).isPresent().contains(reservation);
        verify(jpaReservationRepository, times(1)).findById(id);
    }

    @Test
    void findAll_validReservations_callsReservationRepositoryFindAll() {
        // Arrange
        when(jpaReservationRepository.findAll()).thenReturn(reservationList);

        // Act
        List<Reservation> result = reservationService.findAll();

        // Assert
        assertThat(result).isEqualTo(reservationList);
        verify(jpaReservationRepository, times(1)).findAll();
    }

    @Test
    void createReservation_validReservation_callsReservationRepositoryStore() {
        // Arrange
        when(jpaReservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        // Act
        Reservation result = reservationService.createReservation("The Lord of the Rings", "Franta Vopršálek");

        // Assert
        assertThat(result).isEqualTo(reservation);
        verify(jpaReservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void updateById_existingId_callsReservationRepositoryUpdateById() {
        // Arrange
        Long id = 1L;
        String newBook = "Updated Book";
        String newReservedBy = "Updated User";
        OffsetDateTime newReservedFrom = TimeProvider.now().plusDays(1);
        OffsetDateTime newReservedTo = TimeProvider.now().plusDays(4);when(jpaReservationRepository.updateById(id, newBook, newReservedBy, newReservedFrom, newReservedTo)).thenReturn(1);

        // Act
        int result = reservationService.updateById(id, newBook, newReservedBy, newReservedFrom, newReservedTo);

        // Assert
        assertThat(result).isEqualTo(1);
        verify(jpaReservationRepository, times(1)).updateById(id, newBook, newReservedBy, newReservedFrom, newReservedTo);
    }

    @Test
    void updateById_nonExistingId_returnsEmptyOptional() {
        // Arrange
        Long id = 1L;
        String newBook = "New Book";
        String newReservedBy = "New User";
        OffsetDateTime newReservedFrom = TimeProvider.now().plusDays(1);
        OffsetDateTime newReservedTo = TimeProvider.now().plusDays(4);
        when(jpaReservationRepository.updateById(id, newBook, newReservedBy, newReservedFrom, newReservedTo)).thenReturn(0);

        // Act
        int result = reservationService.updateById(id, newBook, newReservedBy, newReservedFrom, newReservedTo);

        // Assert
        assertThat(result).isEqualTo(0);
    }

    @Test
    void deleteById_validId_callsReservationRepositoryDeleteById() {
        // Arrange
        Long id = 1L;
        doNothing().when(jpaReservationRepository).deleteById(id);

        // Act
        reservationService.deleteById(id);

        // Assert
        verify(jpaReservationRepository, times(1)).deleteById(id);
    }

    @Test
    void findAllActive_callsReservationRepositoryFindAllActive() {
        // Arrange
        List<Reservation> activeReservations = Arrays.asList(
                new Reservation("Active Book 1", "User 1", TimeProvider.now(), TimeProvider.now().plusDays(3)),
                new Reservation("Active Book 2", "User 2", TimeProvider.now(), TimeProvider.now().plusDays(5))
        );
        when(jpaReservationRepository.findAllActive(any(OffsetDateTime.class))).thenReturn(activeReservations);

        // Act
        List<Reservation> result = reservationService.findAllActive();

        // Assert
        assertThat(result).isEqualTo(activeReservations);
        verify(jpaReservationRepository, times(1)).findAllActive(any(OffsetDateTime.class));
    }

    @Test
    void findAllExpired_callsReservationRepositoryFindAllExpired() {
        // Arrange
        List<Reservation> expiredReservations = Arrays.asList(
                new Reservation("Expired Book 1", "User 1", TimeProvider.now().minusDays(5), TimeProvider.now().minusDays(2)),
                new Reservation("Expired Book 2", "User 2", TimeProvider.now().minusDays(3), TimeProvider.now().minusDays(1))
        );
        when(reservationRepository.findAllExpired()).thenReturn(expiredReservations);

        // Act
        List<Reservation> result = reservationService.findAllExpired();

        // Assert
        assertThat(result).isEqualTo(expiredReservations);
        verify(reservationRepository, times(1)).findAllExpired();
    }
}
