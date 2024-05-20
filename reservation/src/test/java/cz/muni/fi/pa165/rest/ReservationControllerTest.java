package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.ReservationFacade;
import cz.muni.fi.pa165.util.ReservationDTOFactory;
import cz.muni.fi.pa165.util.TimeProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.ReservationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author Maxmilián Šeffer
 */
@ExtendWith(MockitoExtension.class)
public class ReservationControllerTest {
    @Mock
    private ReservationFacade reservationFacade;

    @InjectMocks
    private ReservationController reservationController;

    @Test
    void createReservation_validRequestBody_returnsCreated() {
        // Arrange
        Long id = 1L;
        Long bookId = 2L;
        Long reserveeId = 2L;
        ReservationDTO reservation = ReservationDTOFactory.createReservation(id, bookId, reserveeId, TimeProvider.now(), TimeProvider.now().plusDays(3));
        when(reservationFacade.createReservation(bookId, reserveeId)).thenReturn(reservation);

        // Act
        ResponseEntity<ReservationDTO> response = reservationController.createReservation(bookId, reserveeId);

        // Assert
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(reservation);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void deleteReservation_validId_returnsNoContent() {
        // Arrange
        Long id = 1L;

        // Act
        ResponseEntity<Void> response = reservationController.deleteReservation(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(reservationFacade, times(1)).deleteById(id);
    }

    @Test
    void getActiveReservations_validReservations_returnsOK() {
        // Arrange
        List<ReservationDTO> activeReservations = new ArrayList<>();
        when(reservationFacade.findAllActive()).thenReturn(activeReservations);

        // Act
        ResponseEntity<List<ReservationDTO>> response = reservationController.getActiveReservations();

        // Assert
        assertThat(response.getBody()).isEqualTo(activeReservations);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getExpiredReservations_validReservations_returnsExpiredReservations() {
        // Arrange
        List<ReservationDTO> expiredReservations = new ArrayList<>();
        when(reservationFacade.findAllExpired()).thenReturn(expiredReservations);

        // Act
        ResponseEntity<List<ReservationDTO>> response = reservationController.getExpiredReservations();

        // Assert
        assertThat(response.getBody()).isEqualTo(expiredReservations);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getReservation_existingId_returnsOk() {
        // Arrange
        Long id = 1L;
        Long bookId = 5L;
        Long reserveeId = 5L;
        ReservationDTO reservation = ReservationDTOFactory.createReservation(id, bookId, reserveeId, TimeProvider.now(), TimeProvider.now().plusDays(3));
        when(reservationFacade.findById(id)).thenReturn(reservation);

        // Act
        ResponseEntity<ReservationDTO> response = reservationController.getReservation(id);

        // Assert
        assertThat(response.getBody()).isEqualTo(reservation);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getReservations_validReservations_returnsOK() {
        // Arrange
        List<ReservationDTO> reservations = new ArrayList<>();
        when(reservationFacade.findAll()).thenReturn(reservations);

        // Act
        ResponseEntity<List<ReservationDTO>> response = reservationController.getReservations();

        // Assert
        assertThat(response.getBody()).isEqualTo(reservations);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void updateReservation_validId_returnsOK() {
        // Arrange
        Long id = 1L;
        Long bookId = 3L;
        Long reserveeId = 3L;
        OffsetDateTime reservedFrom = TimeProvider.now().plusDays(1);
        OffsetDateTime reservedTo = TimeProvider.now().plusDays(4);
        ReservationDTO reservationDTO = new ReservationDTO().id(id).bookId(bookId).reservedFrom(reservedFrom).reservedTo(reservedTo).reserveeId(reserveeId);

        when(reservationFacade.updateById(id, bookId, reserveeId, reservedFrom, reservedTo)).thenReturn(reservationDTO);

        // Act
        ResponseEntity<ReservationDTO> response = reservationController.updateReservation(id, bookId, reserveeId, reservedFrom, reservedTo);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(reservationFacade, times(1)).updateById(id, bookId, reserveeId, reservedFrom, reservedTo);
    }
}
