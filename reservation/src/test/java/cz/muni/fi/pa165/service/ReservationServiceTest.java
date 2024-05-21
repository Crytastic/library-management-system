package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.Reservation;
import cz.muni.fi.pa165.data.repository.ReservationRepository;
import cz.muni.fi.pa165.exceptionhandling.exceptions.ConstraintViolationException;
import cz.muni.fi.pa165.exceptionhandling.exceptions.ResourceNotFoundException;
import cz.muni.fi.pa165.util.ServiceHttpRequestProvider;
import cz.muni.fi.pa165.util.TimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author Maxmilián Šeffer
 */
@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ServiceHttpRequestProvider serviceHttpRequestProvider;

    @InjectMocks
    private ReservationService reservationService;

    private Reservation reservation;
    private List<Reservation> reservationList;

    @BeforeEach
    void setUp() {
        reservation = new Reservation(1L, 1L, TimeProvider.now(), TimeProvider.now().plusDays(3));
        reservationList = Arrays.asList(
                new Reservation(2L, 2L, TimeProvider.now(), TimeProvider.now().plusDays(3)),
                new Reservation(3L, 3L, TimeProvider.now(), TimeProvider.now().plusDays(5))
        );
    }

    @Test
    void findById_existingId_callsReservationRepositoryFindByIdAndReturnsReservation() {
        // Arrange
        Long id = 1L;
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation));

        // Act
        Reservation result = reservationService.findById(id);

        // Assert
        assertThat(result).isEqualTo(reservation);
        verify(reservationRepository, times(1)).findById(id);
    }

    @Test
    void findById_nonExistingId_callsReservationRepositoryFindByIdAndThrowsResourceNotFoundException() {
        // Arrange
        Long id = 1L;
        when(reservationRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        Throwable exception = assertThrows(ResourceNotFoundException.class, () -> reservationService.findById(id));

        assertThat(exception.getMessage()).isEqualTo(String.format("Reservation with id: %d not found", id));
        verify(reservationRepository, times(1)).findById(id);
    }

    @Test
    void findAll_validReservations_callsReservationRepositoryFindAll() {
        // Arrange
        when(reservationRepository.findAll()).thenReturn(reservationList);

        // Act
        List<Reservation> result = reservationService.findAll();

        // Assert
        assertThat(result).isEqualTo(reservationList);
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void createReservation_validReservation_callsReservationRepositoryStore() {
        // Arrange
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(serviceHttpRequestProvider.callGetBookById(4L)).thenReturn(new ResponseEntity<>("something", HttpStatus.OK));
        when(serviceHttpRequestProvider.callGetUserById(4L)).thenReturn(new ResponseEntity<>("something", HttpStatus.OK));

        // Act
        Reservation result = reservationService.createReservation(4L, 4L);

        // Assert
        assertThat(result).isEqualTo(reservation);
        verify(reservationRepository, times(1)).save(any(Reservation.class));
    }

    @Test
    void createReservation_bookAlreadyReserved_throwsResourceNotAvailableException() {
        // Arrange
        Long bookId = 1L;
        Long reserveeId = 2L;
        OffsetDateTime now = TimeProvider.now();
        try (MockedStatic<TimeProvider> timeProviderDummy = mockStatic(TimeProvider.class)) {
            timeProviderDummy.when(TimeProvider::now).thenReturn(now);
            Reservation activeReservation = new Reservation(bookId, reserveeId, now.minusDays(1), now.plusDays(2));

            when(reservationRepository.findAllActive(now)).thenReturn(List.of(activeReservation));

        // Act
        Throwable exception = assertThrows(ConstraintViolationException.class, () -> reservationService.createReservation(bookId, reserveeId));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Book already reserved.");
        }
    }

    @Test
    void updateById_existingId_callsReservationRepositoryUpdateById() {
        // Arrange
        Long id = 1L;
        Long newBookId = 11L;
        Long newReserveeId = 11L;
        OffsetDateTime newReservedFrom = TimeProvider.now().plusDays(1);
        OffsetDateTime newReservedTo = TimeProvider.now().plusDays(4);
        Reservation reservation = new Reservation();
        reservation.setId(id);
        reservation.setBookId(newBookId);
        reservation.setReserveeId(newReserveeId);
        reservation.setReservedFrom(newReservedFrom);
        reservation.setReservedTo(newReservedTo);
        when(reservationRepository.updateById(id, newBookId, newReserveeId, newReservedFrom, newReservedTo)).thenReturn(1);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        // Act
        Reservation result = reservationService.updateById(id, newBookId, newReserveeId, newReservedFrom, newReservedTo);

        // Assert
        assertThat(result).isEqualTo(reservation);
        verify(reservationRepository, times(1)).updateById(id, newBookId, newReserveeId, newReservedFrom, newReservedTo);
    }

    @Test
    void updateById_nonExistingId_throwsResourceNotFoundException() {
        // Arrange
        Long id = 1L;
        Long newBookId = 11L;
        Long newReserveeId = 11L;
        OffsetDateTime newReservedFrom = TimeProvider.now().plusDays(1);
        OffsetDateTime newReservedTo = TimeProvider.now().plusDays(4);
        when(reservationRepository.updateById(id, newBookId, newReserveeId, newReservedFrom, newReservedTo)).thenReturn(0);

        // Act
        Throwable exception = assertThrows(ResourceNotFoundException.class, () -> reservationService.updateById(id, newBookId, newReserveeId, newReservedFrom, newReservedTo));

        // Assert
        assertThat(exception.getMessage()).isEqualTo(String.format("Reservation with id: %d not found", id));
    }

    @Test
    void deleteById_validId_callsReservationRepositoryDeleteById() {
        // Arrange
        Long id = 1L;
        doNothing().when(reservationRepository).deleteById(id);

        // Act
        reservationService.deleteById(id);

        // Assert
        verify(reservationRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteAll_allReservationsDelete_callsReservationRepositoryDeleteAll() {
        // Arrange
        doNothing().when(reservationRepository).deleteAll();

        // Act
        reservationService.deleteAll();

        // Assert
        verify(reservationRepository, times(1)).deleteAll();
    }

    @Test
    void findAllActive_callsReservationRepositoryFindAllActive() {
        // Arrange
        List<Reservation> activeReservations = Arrays.asList(
                new Reservation(5L, 5L, TimeProvider.now(), TimeProvider.now().plusDays(3)),
                new Reservation(6L, 6L, TimeProvider.now(), TimeProvider.now().plusDays(5))
        );
        when(reservationRepository.findAllActive(any(OffsetDateTime.class))).thenReturn(activeReservations);

        // Act
        List<Reservation> result = reservationService.findAllActive();

        // Assert
        assertThat(result).isEqualTo(activeReservations);
        verify(reservationRepository, times(1)).findAllActive(any(OffsetDateTime.class));
    }

    @Test
    void findAllExpired_callsReservationRepositoryFindAllExpired() {
        // Arrange
        List<Reservation> expiredReservations = Arrays.asList(
                new Reservation(7L, 7L, TimeProvider.now().minusDays(5), TimeProvider.now().minusDays(2)),
                new Reservation(8L, 8L, TimeProvider.now().minusDays(3), TimeProvider.now().minusDays(1))
        );
        when(reservationRepository.findAllExpired(any(OffsetDateTime.class))).thenReturn(expiredReservations);

        // Act
        List<Reservation> result = reservationService.findAllExpired();

        // Assert
        assertThat(result).isEqualTo(expiredReservations);
        verify(reservationRepository, times(1)).findAllExpired(any(OffsetDateTime.class));
    }
}
