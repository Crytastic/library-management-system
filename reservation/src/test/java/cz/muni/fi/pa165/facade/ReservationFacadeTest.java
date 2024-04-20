package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.data.model.Reservation;
import cz.muni.fi.pa165.service.ReservationService;
import cz.muni.fi.pa165.util.TimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.ReservationDTO;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Maxmilián Šeffer
 */
@ExtendWith(MockitoExtension.class)
class ReservationFacadeTest {

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationFacade reservationFacade;

    private Reservation reservation;
    private Reservation expiredReservation;

    @BeforeEach
    void setUp() {
        reservation = new Reservation("The Lord of the Rings", "John Doe", TimeProvider.now(), TimeProvider.now().plusDays(1));
        expiredReservation = new Reservation("The Hobbit", "Franta Vopršálek", TimeProvider.now().minusDays(4), TimeProvider.now().minusDays(1));
    }

    @Test
    void findAll_existingReservations_callsFindAllOnReservationService() {
        // Arrange
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        when(reservationService.findAll()).thenReturn(reservations);

        // Act
        List<ReservationDTO> result = reservationFacade.findAll();

        // Assert
        assertThat(result).hasSize(1);
        verify(reservationService, times(1)).findAll();
    }

    @Test
    void createReservation_validParameters_callsCreateOnReservationService() {
        // Arrange
        String book = "The Lord of the Rings";
        String reservedBy = "John Doe";
        when(reservationService.createReservation(book, reservedBy)).thenReturn(reservation);

        // Act
        ReservationDTO result = reservationFacade.createReservation(book, reservedBy);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getBook()).isEqualTo(book);
        assertThat(result.getReservedBy()).isEqualTo(reservedBy);
        verify(reservationService, times(1)).createReservation(book, reservedBy);
    }

    @Test
    void findById_reservationExists_returnsUpdatedReservationDTO() {
        // Arrange
        Long id = 1L;
        when(reservationService.findById(id)).thenReturn(Optional.of(reservation));

        // Act
        Optional<ReservationDTO> result = reservationFacade.findById(id);

        // Assert
        assertThat(result).isPresent();
        verify(reservationService, times(1)).findById(id);
    }

    @Test
    void findById_reservationDoesNotExist_returnsEmptyOptional() {
        // Arrange
        Long id = 1L;
        when(reservationService.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<ReservationDTO> result = reservationFacade.findById(id);

        // Assert
        assertThat(result).isEmpty();
        verify(reservationService, times(1)).findById(id);
    }

    @Test
    void updateById_validParameters_returnsUpdatedReservationDTO() {
        // Arrange
        Long id = 1L;
        OffsetDateTime reservedFrom = TimeProvider.now();
        OffsetDateTime reservedTo = TimeProvider.now().plusDays(1);
        when(reservationService.updateById(id, reservation.getBook(), reservation.getReservedBy(), reservedFrom, reservedTo)).thenReturn(Optional.of(reservation));

        // Act
        Optional<ReservationDTO> result = reservationFacade.updateById(id, reservation.getBook(), reservation.getReservedBy(), reservedFrom, reservedTo);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getBook()).isEqualTo(reservation.getBook());
        assertThat(result.get().getReservedBy()).isEqualTo(reservation.getReservedBy());
        assertThat(result.get().getReservedFrom()).isEqualTo(reservation.getReservedFrom());
        assertThat(result.get().getReservedTo()).isEqualTo(reservation.getReservedTo());
        verify(reservationService, times(1)).updateById(id, reservation.getBook(), reservation.getReservedBy(), reservedFrom, reservedTo);
    }

    @Test
    void updateById_invalidId_returnsEmptyOptional() {
        // Arrange
        Long id = 1L;
        OffsetDateTime reservedFrom = TimeProvider.now();
        OffsetDateTime reservedTo = TimeProvider.now().plusDays(1);
        when(reservationService.updateById(id, reservation.getBook(), reservation.getReservedBy(), reservedFrom, reservedTo)).thenReturn(Optional.empty());

        // Act
        Optional<ReservationDTO> result = reservationFacade.updateById(id, reservation.getBook(), reservation.getReservedBy(), reservedFrom, reservedTo);

        // Assert
        assertThat(result).isEmpty();
        verify(reservationService, times(1)).updateById(id, reservation.getBook(), reservation.getReservedBy(), reservedFrom, reservedTo);
    }

    @Test
    void deleteById_validId_callsDeleteByIdOnReservationService() {
        // Arrange
        Long id = 1L;

        // Act
        reservationFacade.deleteById(id);

        // Assert
        verify(reservationService, times(1)).deleteById(id);
    }

    @Test
    void findAllActive_validReservations_returnsListOfActiveReservations() {
        // Arrange
        List<Reservation> activeReservations = new ArrayList<>();
        activeReservations.add(reservation);
        when(reservationService.findAllActive()).thenReturn(activeReservations);

        // Act
        List<ReservationDTO> result = reservationFacade.findAllActive();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getBook()).isEqualTo(reservation.getBook());
        assertThat(result.getFirst().getReservedBy()).isEqualTo(reservation.getReservedBy());
        assertThat(result.getFirst().getReservedFrom()).isEqualTo(reservation.getReservedFrom());
        assertThat(result.getFirst().getReservedTo()).isEqualTo(reservation.getReservedTo());
        verify(reservationService, times(1)).findAllActive();
    }

    @Test
    void findAllExpired_validReservations_returnsListOfExpiredReservations() {
        // Arrange
        List<Reservation> expiredReservations = new ArrayList<>();
        expiredReservations.add(expiredReservation);
        when(reservationService.findAllExpired()).thenReturn(expiredReservations);

        // Act
        List<ReservationDTO> result = reservationFacade.findAllExpired();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getBook()).isEqualTo(expiredReservation.getBook());
        assertThat(result.getFirst().getReservedBy()).isEqualTo(expiredReservation.getReservedBy());
        assertThat(result.getFirst().getReservedFrom()).isEqualTo(expiredReservation.getReservedFrom());
        assertThat(result.getFirst().getReservedTo()).isEqualTo(expiredReservation.getReservedTo());
        verify(reservationService, times(1)).findAllExpired();
    }
}
