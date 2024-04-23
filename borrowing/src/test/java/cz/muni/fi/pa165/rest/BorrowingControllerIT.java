package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.data.model.Borrowing;
import cz.muni.fi.pa165.data.repository.BorrowingRepository;
import cz.muni.fi.pa165.util.ObjectConverter;
import cz.muni.fi.pa165.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.openapitools.model.BorrowingDTO;
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
public class BorrowingControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BorrowingRepository borrowingRepository;

    @Test
    void getBorrowing_existingId_returnsOk() throws Exception {
        // Arrange
        Long id = 1L;
        Borrowing borrowing = TestDataFactory.activeBorrowing;
        borrowing.setId(id);
        borrowingRepository.save(borrowing);

        // Act
        String responseJson = mockMvc.perform(get("/api/borrowings/{id}", id)
                        // Assert
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        BorrowingDTO response = ObjectConverter.convertJsonToObject(responseJson, BorrowingDTO.class);


        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getBookId()).isEqualTo(borrowing.getBookId());
        assertThat(response.getBorrowerId()).isEqualTo(borrowing.getBorrowerId());
        assertThat(response.getBorrowDate()).isEqualTo(borrowing.getBorrowDate());
        assertThat(response.getExpectedReturnDate()).isEqualTo(borrowing.getExpectedReturnDate());
        assertThat(response.getReturned()).isEqualTo(borrowing.isReturned());
        assertThat(response.getLateReturnWeeklyFine().stripTrailingZeros()).isEqualTo(borrowing.getLateReturnWeeklyFine());
        assertThat(response.getFineResolved()).isEqualTo(borrowing.isFineResolved());
    }

    @Test
    void getBorrowing_invalidId_returnsNotFound() throws Exception {
        // Arrange
        Long id = 10L;

        // Act and Assert
        mockMvc.perform(get("/api/borrowings/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }
}
