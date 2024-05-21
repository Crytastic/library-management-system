package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.data.model.Reservation;
import cz.muni.fi.pa165.exceptionhandling.exceptions.ResourceNotFoundException;
import cz.muni.fi.pa165.mappers.ReservationMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author Maxmilián Šeffer
 */
@ExtendWith(MockitoExtension.class)
class ReservationFacadeTest {
    @Mock
    private ReservationService reservationService;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationFacade reservationFacade;

    private Reservation reservation;
    private Reservation expiredReservation;
    private ReservationDTO reservationDTO;
    private ReservationDTO expiredReservationDTO;

    @BeforeEach
    void setUp() {
        OffsetDateTime now = TimeProvider.now();
        OffsetDateTime nowPlusOneDay = TimeProvider.now().plusDays(1);
        OffsetDateTime nowMinusFourDays = TimeProvider.now().minusDays(4);
        OffsetDateTime nowMinusOneDay = TimeProvider.now().minusDays(1);

        reservation = new Reservation(1L, 1L, now, nowPlusOneDay);
        expiredReservation = new Reservation(2L, 2L, nowMinusFourDays, nowMinusOneDay);

        reservationDTO = new ReservationDTO().bookId(1L).reserveeId(1L).reservedFrom(now).reservedTo(nowPlusOneDay);
        expiredReservationDTO = new ReservationDTO().bookId(2L).reserveeId(2L).reservedFrom(nowMinusFourDays).reservedTo(nowMinusOneDay);
    }

    @Test
    void findAll_existingReservations_callsFindAllOnReservationService() {
        // Arrange
        List<Reservation> reservations = new ArrayList<>();
        reservations.add(reservation);
        when(reservationService.findAll()).thenReturn(reservations);
        when(reservationMapper.mapToList(reservations)).thenReturn(List.of(reservationDTO));

        // Act
        List<ReservationDTO> result = reservationFacade.findAll();

        // Assert
        assertThat(result).hasSize(1);
        verify(reservationService, times(1)).findAll();
    }

    @Test
    void createReservation_validParameters_callsCreateOnReservationService() {
        // Arrange
        Long bookId = reservation.getBookId();
        Long reserveeId = reservation.getReserveeId();
        when(reservationService.createReservation(bookId, reserveeId)).thenReturn(reservation);
        when(reservationMapper.mapToDto(reservation)).thenReturn(reservationDTO);

        // Act
        ReservationDTO result = reservationFacade.createReservation(bookId, reserveeId);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getBookId()).isEqualTo(bookId);
        assertThat(result.getReserveeId()).isEqualTo(reserveeId);
        verify(reservationService, times(1)).createReservation(bookId, reserveeId);
    }

    @Test
    void findById_reservationExists_returnsUpdatedReservationDTO() {
        // Arrange
        Long id = 1L;
        when(reservationService.findById(id)).thenReturn(reservation);
        when(reservationMapper.mapToDto(reservation)).thenReturn(reservationDTO);

        // Act
        ReservationDTO result = reservationFacade.findById(id);

        // Assert
        assertThat(result).isEqualTo(reservationDTO);
        verify(reservationService, times(1)).findById(id);
    }

    @Test
    void findById_reservationDoesNotExist_throwsResourceNotFoundException() {
        // Arrange
        Long id = 1L;
        when(reservationService.findById(id)).thenThrow(new ResourceNotFoundException(String.format("Reservation with id: %d not found", id)));

        // Act + Assert
        Throwable exception = assertThrows(ResourceNotFoundException.class, () -> reservationFacade.findById(id));

        assertThat(exception.getMessage()).isEqualTo(String.format("Reservation with id: %d not found", id));
        verify(reservationService, times(1)).findById(id);
    }

    @Test
    void updateById_validParameters_returnsUpdatedReservation() {
        // Arrange
        Long id = 1L;
        OffsetDateTime reservedFrom = TimeProvider.now();
        OffsetDateTime reservedTo = TimeProvider.now().plusDays(1);
        Reservation updatedReservation = new Reservation();
        updatedReservation.setId(id);
        updatedReservation.setReservedFrom(reservedFrom);
        updatedReservation.setReservedTo(reservedTo);
        updatedReservation.setBookId(reservation.getBookId());
        updatedReservation.setReserveeId(reservation.getReserveeId());
        ReservationDTO updatedReservationDTO = new ReservationDTO().bookId(id).reservedFrom(reservedFrom).reservedTo(reservedTo).bookId(reservation.getBookId()).reserveeId(reservation.getReserveeId());

        when(reservationService.updateById(id, reservation.getBookId(), reservation.getReserveeId(), reservedFrom, reservedTo)).thenReturn(updatedReservation);
        when(reservationMapper.mapToDto(updatedReservation)).thenReturn(updatedReservationDTO);

        // Act
        ReservationDTO result = reservationFacade.updateById(id, reservation.getBookId(), reservation.getReserveeId(), reservedFrom, reservedTo);

        // Assert
        assertThat(result).isEqualTo(updatedReservationDTO);

        verify(reservationService, times(1)).updateById(id, reservation.getBookId(), reservation.getReserveeId(), reservedFrom, reservedTo);
    }

    @Test
    void updateById_invalidId_throwsResourceNotFoundException() {
        // Arrange
        Long id = 1L;
        OffsetDateTime reservedFrom = TimeProvider.now();
        OffsetDateTime reservedTo = TimeProvider.now().plusDays(1);
        when(reservationService.updateById(id, reservation.getBookId(), reservation.getReserveeId(), reservedFrom, reservedTo)).thenThrow(new ResourceNotFoundException(String.format("Reservation with id: %d not found", id)));

        // Act + Assert
        Throwable exception = assertThrows(ResourceNotFoundException.class, () -> reservationFacade.updateById(id, reservation.getBookId(), reservation.getReserveeId(), reservedFrom, reservedTo));

        assertThat(exception.getMessage()).isEqualTo(String.format("Reservation with id: %d not found", id));
        verify(reservationService, times(1)).updateById(id, reservation.getBookId(), reservation.getReserveeId(), reservedFrom, reservedTo);
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
    void deleteAll_allReservationsDelete_callsDeleteAllOnReservationService() {
        // Act
        reservationFacade.deleteAll();

        // Assert
        verify(reservationService, times(1)).deleteAll();
    }

    @Test
    void findAllActive_validReservations_returnsListOfActiveReservations() {
        // Arrange
        List<Reservation> activeReservations = new ArrayList<>();
        activeReservations.add(reservation);
        when(reservationService.findAllActive()).thenReturn(activeReservations);
        when(reservationMapper.mapToList(activeReservations)).thenReturn(List.of(reservationDTO));

        // Act
        List<ReservationDTO> result = reservationFacade.findAllActive();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getBookId()).isEqualTo(reservation.getBookId());
        assertThat(result.getFirst().getReserveeId()).isEqualTo(reservation.getReserveeId());
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
        when(reservationMapper.mapToList(expiredReservations)).thenReturn(List.of(expiredReservationDTO));

        // Act
        List<ReservationDTO> result = reservationFacade.findAllExpired();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getBookId()).isEqualTo(expiredReservation.getBookId());
        assertThat(result.getFirst().getReserveeId()).isEqualTo(expiredReservation.getReserveeId());
        assertThat(result.getFirst().getReservedFrom()).isEqualTo(expiredReservation.getReservedFrom());
        assertThat(result.getFirst().getReservedTo()).isEqualTo(expiredReservation.getReservedTo());
        verify(reservationService, times(1)).findAllExpired();
    }
}
