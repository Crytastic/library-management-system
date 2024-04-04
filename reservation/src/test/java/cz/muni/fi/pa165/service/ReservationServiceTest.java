package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.dao.ReservationDAO;
import cz.muni.fi.pa165.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
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
}
