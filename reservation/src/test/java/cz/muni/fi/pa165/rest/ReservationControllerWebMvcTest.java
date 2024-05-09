package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.exceptionhandling.ApiError;
import cz.muni.fi.pa165.exceptionhandling.exceptions.ResourceNotFoundException;
import cz.muni.fi.pa165.facade.ReservationFacade;
import cz.muni.fi.pa165.util.ObjectConverter;
import cz.muni.fi.pa165.util.ReservationDTOFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openapitools.model.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ReservationController.class})
public class ReservationControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationFacade reservationFacade;

    @MockBean
    SecurityFilterChain securityFilterChain;

    @MockBean
    OpaqueTokenIntrospector opaqueTokenIntrospector;

    @Test
    void getReservation_existingId_returnsOk() throws Exception {
        // Arrange
        Long id = 1L;
        Long bookId = 1L;
        Long reserveeId = 1L;
        String reservedFromString = "2024-04-21T20:29:59.123456Z";
        OffsetDateTime reservedFrom = OffsetDateTime.parse(reservedFromString);
        String reservedToString = "2024-04-24T20:29:59.123456Z";
        OffsetDateTime reservedTo = OffsetDateTime.parse(reservedToString);
        ReservationDTO reservationDTO = ReservationDTOFactory.createReservation(id, bookId, reserveeId, reservedFrom, reservedTo);
        when(reservationFacade.findById(id)).thenReturn(reservationDTO);

        // Act
        String responseJson = mockMvc.perform(get("/api/reservations/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        ReservationDTO response = ObjectConverter.convertJsonToObject(responseJson, ReservationDTO.class);

        // Assert
        assertThat(responseJson).isEqualTo(String.format("{\"id\":1,\"bookId\":%d,\"reserveeId\":%d,\"reservedFrom\":\"%s\",\"reservedTo\":\"%s\"}", bookId, reserveeId, reservedFromString, reservedToString));
        assertThat(response).isEqualTo(reservationDTO);
    }

    @Test
    void updateReservation_invalidId_returnsNotFound() throws Exception {
        // Arrange
        Long id = 10L;
        Mockito.when(reservationFacade.findById(id)).thenThrow(new ResourceNotFoundException(String.format("Reservation with id: %d not found", id)));

        // Act and Assert
        String responseJson = mockMvc.perform(get("/api/reservations/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        ApiError error = ObjectConverter.convertJsonToObject(responseJson, ApiError.class);

        assertThat(error.getMessage()).isEqualTo(String.format("Reservation with id: %d not found", id));
    }
}
