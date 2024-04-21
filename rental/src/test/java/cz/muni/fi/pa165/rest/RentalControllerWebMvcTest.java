package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.RentalFacade;
import cz.muni.fi.pa165.util.ObjectConverter;
import cz.muni.fi.pa165.util.RentalDTOFactory;
import org.junit.jupiter.api.Test;
import org.openapitools.model.RentalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {RentalController.class})
public class RentalControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RentalFacade rentalFacade;

    @Test
    void getRental_existingId_returnsOk() throws Exception {
        // Arrange
        Long id = 1L;
        String book = "The Lord of the Rings";
        String rentedBy = "Franta Vopršálek";
        String borrowDateFromString = "2024-04-21T20:29:59.123456Z";
        OffsetDateTime borrowDate = OffsetDateTime.parse(borrowDateFromString);
        OffsetDateTime expectedReturnDate = borrowDate.plusMonths(3);
        BigDecimal lateReturnWeeklyFine = new BigDecimal(100);
        RentalDTO rentalDTO = RentalDTOFactory.createRental(id,
                book,
                rentedBy,
                borrowDate,
                expectedReturnDate,
                false,
                null,
                lateReturnWeeklyFine,
                false);
        when(rentalFacade.findById(id)).thenReturn(Optional.of(rentalDTO));

        // Act
        String responseJson = mockMvc.perform(get("/api/rentals/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        RentalDTO response = ObjectConverter.convertJsonToObject(responseJson, RentalDTO.class);

        // Assert

        assertThat(responseJson).isEqualTo(String.format("{\"id\":1,\"book\":\"%s\",\"rentedBy\":\"%s\",\"borrowDate\":\"%s\",\"expectedReturnDate\":\"%s\",\"returned\":%b,\"returnDate\":%s,\"lateReturnWeeklyFine\":100,\"fineResolved\":false}", book, rentedBy, borrowDate, expectedReturnDate, false, null));
        assertThat(response).isEqualTo(rentalDTO);
    }

    @Test
    void updateRental_invalidId_returnsNotFound() throws Exception {
        // Arrange
        Long id = 10L;
        when(rentalFacade.findById(id)).thenReturn(Optional.empty());

        // Act and Assert
        String responseJson = mockMvc.perform(get("/api/rentals/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(responseJson).isEqualTo("");
    }
}
