package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.data.model.Rental;
import cz.muni.fi.pa165.data.repository.RentalRepository;
import cz.muni.fi.pa165.util.ObjectConverter;
import cz.muni.fi.pa165.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.openapitools.model.RentalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RentalControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RentalRepository rentalRepository;

    @Test
    void getRental_existingId_returnsOk() throws Exception {
        // Arrange
        Long id = 1L;
        Rental rental = TestDataFactory.activeRental;
        rental.setId(id);
        rentalRepository.save(rental);

        // Act
        String responseJson = mockMvc.perform(get("/api/rentals/{id}", id)
                        // Assert
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        RentalDTO response = ObjectConverter.convertJsonToObject(responseJson, RentalDTO.class);


        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getBookId()).isEqualTo(rental.getBookId());
        assertThat(response.getBorrowerId()).isEqualTo(rental.getBorrowerId());
        assertThat(response.getBorrowDate()).isEqualTo(rental.getBorrowDate());
        assertThat(response.getExpectedReturnDate()).isEqualTo(rental.getExpectedReturnDate());
        assertThat(response.getReturned()).isEqualTo(rental.isReturned());
        assertThat(response.getLateReturnWeeklyFine().stripTrailingZeros()).isEqualTo(rental.getLateReturnWeeklyFine());
        assertThat(response.getFineResolved()).isEqualTo(rental.isFineResolved());
    }

    @Test
    void getRental_invalidId_returnsNotFound() throws Exception {
        // Arrange
        Long id = 10L;

        // Act and Assert
        mockMvc.perform(get("/api/rentals/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }
}
