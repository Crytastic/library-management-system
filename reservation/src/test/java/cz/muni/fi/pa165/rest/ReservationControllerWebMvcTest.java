package cz.muni.fi.pa165.rest;

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
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Optional;

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

    @Test
    void getReservation_existingId_returnsOk() throws Exception {
        // Arrange
        Long id = 1L;
        String book = "The Lord of the Rings";
        String reservedBy = "Franta Vopršálek";
        String reservedFromString = "2024-04-21T20:29:59.123456Z";
        OffsetDateTime reservedFrom = OffsetDateTime.parse(reservedFromString);
        String reservedToString = "2024-04-24T20:29:59.123456Z";
        OffsetDateTime reservedTo = OffsetDateTime.parse(reservedToString);
        ReservationDTO reservationDTO = ReservationDTOFactory.createReservation(id, book, reservedBy, reservedFrom, reservedTo);
        when(reservationFacade.findById(id)).thenReturn(Optional.of(reservationDTO));

        // Act
        String responseJson = mockMvc.perform(get("/api/reservations/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        ReservationDTO response = ObjectConverter.convertJsonToObject(responseJson, ReservationDTO.class);

        // Assert
        assertThat(responseJson).isEqualTo(String.format("{\"id\":1,\"book\":\"The Lord of the Rings\",\"reservedBy\":\"Franta Vopršálek\",\"reservedFrom\":\"%s\",\"reservedTo\":\"%s\"}", reservedFromString, reservedToString));
        assertThat(response).isEqualTo(reservationDTO);
    }

    @Test
    void updateReservation_invalidId_returnsNotFound() throws Exception {
        // Arrange
        Long id = 10L;
        Mockito.when(reservationFacade.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        String responseJson = mockMvc.perform(get("/api/reservations/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(responseJson).isEqualTo("");
    }

}
