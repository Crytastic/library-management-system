package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.dao.ReservationDAO;
import cz.muni.fi.pa165.service.ReservationService;
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

    private ReservationDAO reservationDAO;
    private ReservationDAO expiredReservationDAO;

    @BeforeEach
    void setUp() {
        reservationDAO = new ReservationDAO("The Lord of the Rings", "John Doe", OffsetDateTime.now(), OffsetDateTime.now().plusDays(1));
        expiredReservationDAO = new ReservationDAO("The Hobbit", "Franta Vopršálek", OffsetDateTime.now().minusDays(4), OffsetDateTime.now().minusDays(1));
    }

    @Test
    void findAll_existingReservations_callsFindAllOnReservationService() {
        // Arrange
        List<ReservationDAO> reservations = new ArrayList<>();
        reservations.add(reservationDAO);
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
        when(reservationService.createReservation(book, reservedBy)).thenReturn(reservationDAO);

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
        when(reservationService.findById(id)).thenReturn(Optional.of(reservationDAO));

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
        OffsetDateTime reservedFrom = OffsetDateTime.now();
        OffsetDateTime reservedTo = OffsetDateTime.now().plusDays(1);
        when(reservationService.updateById(id, reservationDAO.getBook(), reservationDAO.getReservedBy(), reservedFrom, reservedTo)).thenReturn(Optional.of(reservationDAO));

        // Act
        Optional<ReservationDTO> result = reservationFacade.updateById(id, reservationDAO.getBook(), reservationDAO.getReservedBy(), reservedFrom, reservedTo);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getBook()).isEqualTo(reservationDAO.getBook());
        assertThat(result.get().getReservedBy()).isEqualTo(reservationDAO.getReservedBy());
        assertThat(result.get().getReservedFrom()).isEqualTo(reservationDAO.getReservedFrom());
        assertThat(result.get().getReservedTo()).isEqualTo(reservationDAO.getReservedTo());
        verify(reservationService, times(1)).updateById(id, reservationDAO.getBook(), reservationDAO.getReservedBy(), reservedFrom, reservedTo);
    }

    @Test
    void updateById_invalidId_returnsEmptyOptional() {
        // Arrange
        Long id = 1L;
        OffsetDateTime reservedFrom = OffsetDateTime.now();
        OffsetDateTime reservedTo = OffsetDateTime.now().plusDays(1);
        when(reservationService.updateById(id, reservationDAO.getBook(), reservationDAO.getReservedBy(), reservedFrom, reservedTo)).thenReturn(Optional.empty());

        // Act
        Optional<ReservationDTO> result = reservationFacade.updateById(id, reservationDAO.getBook(), reservationDAO.getReservedBy(), reservedFrom, reservedTo);

        // Assert
        assertThat(result).isEmpty();
        verify(reservationService, times(1)).updateById(id, reservationDAO.getBook(), reservationDAO.getReservedBy(), reservedFrom, reservedTo);
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
        List<ReservationDAO> activeReservations = new ArrayList<>();
        activeReservations.add(reservationDAO);
        when(reservationService.findAllActive()).thenReturn(activeReservations);

        // Act
        List<ReservationDTO> result = reservationFacade.findAllActive();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getBook()).isEqualTo(reservationDAO.getBook());
        assertThat(result.getFirst().getReservedBy()).isEqualTo(reservationDAO.getReservedBy());
        assertThat(result.getFirst().getReservedFrom()).isEqualTo(reservationDAO.getReservedFrom());
        assertThat(result.getFirst().getReservedTo()).isEqualTo(reservationDAO.getReservedTo());
        verify(reservationService, times(1)).findAllActive();
    }

    @Test
    void findAllExpired_validReservations_returnsListOfExpiredReservations() {
        // Arrange
        List<ReservationDAO> expiredReservations = new ArrayList<>();
        expiredReservations.add(expiredReservationDAO);
        when(reservationService.findAllExpired()).thenReturn(expiredReservations);

        // Act
        List<ReservationDTO> result = reservationFacade.findAllExpired();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getBook()).isEqualTo(expiredReservationDAO.getBook());
        assertThat(result.getFirst().getReservedBy()).isEqualTo(expiredReservationDAO.getReservedBy());
        assertThat(result.getFirst().getReservedFrom()).isEqualTo(expiredReservationDAO.getReservedFrom());
        assertThat(result.getFirst().getReservedTo()).isEqualTo(expiredReservationDAO.getReservedTo());
        verify(reservationService, times(1)).findAllExpired();
    }
}
