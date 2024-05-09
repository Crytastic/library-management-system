package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.data.model.Reservation;
import cz.muni.fi.pa165.data.repository.ReservationRepository;
import cz.muni.fi.pa165.exceptionhandling.ApiError;
import cz.muni.fi.pa165.util.ObjectConverter;
import cz.muni.fi.pa165.util.TimeProvider;
import org.junit.jupiter.api.Test;
import org.openapitools.model.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ReservationControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void getReservation_existingId_returnsOk() throws Exception {
        // Arrange
        Long id = 1L;
        Long bookId = 2L;
        Long reserveeId = 2L;
        OffsetDateTime reservedFrom = TimeProvider.now().plusDays(1);
        OffsetDateTime reservedTo = TimeProvider.now().plusDays(4);
        Reservation reservation = new Reservation(bookId, reserveeId, reservedFrom, reservedTo);
        reservation.setId(id);
        reservationRepository.save(reservation);


        // Act
        String responseJson = mockMvc.perform(get("/api/reservations/{id}", id)
                        // Assert
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        ReservationDTO response = ObjectConverter.convertJsonToObject(responseJson, ReservationDTO.class);


        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getBookId()).isEqualTo(bookId);
        assertThat(response.getReserveeId()).isEqualTo(reserveeId);
        assertThat(response.getReservedFrom().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(reservedFrom.withOffsetSameInstant(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS));
        assertThat(response.getReservedTo().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(reservedTo.withOffsetSameInstant(ZoneOffset.UTC).truncatedTo(ChronoUnit.MILLIS));
    }

    @Test
    void getReservation_invalidId_returnsNotFound() throws Exception {
        // Arrange
        Long id = 10L;

        // Act and Assert
        String responseJson = mockMvc.perform(get("/api/reservations/{id}", id)
                        // Assert
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        ApiError error = ObjectConverter.convertJsonToObject(responseJson, ApiError.class);

        assertThat(error.getMessage()).isEqualTo(String.format("Reservation with id: %d not found", id));
    }
}
