package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.exceptionhandling.ApiError;
import cz.muni.fi.pa165.exceptionhandling.exceptions.ResourceNotFoundException;
import cz.muni.fi.pa165.facade.BorrowingFacade;
import cz.muni.fi.pa165.util.BorrowingDTOFactory;
import cz.muni.fi.pa165.util.ObjectConverter;
import org.junit.jupiter.api.Test;
import org.openapitools.model.BorrowingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {BorrowingController.class})
public class BorrowingControllerWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BorrowingFacade borrowingFacade;

    @MockBean
    SecurityFilterChain securityFilterChain;

    @MockBean
    OpaqueTokenIntrospector opaqueTokenIntrospector;

    @Test
    void getBorrowing_existingId_returnsOk() throws Exception {
        // Arrange
        Long id = 1L;
        Long bookId = 1L;
        Long borrowerId = 2L;
        String borrowDateFromString = "2024-04-21T20:29:59.123456Z";
        OffsetDateTime borrowDate = OffsetDateTime.parse(borrowDateFromString);
        OffsetDateTime expectedReturnDate = borrowDate.plusMonths(3);
        BigDecimal lateReturnWeeklyFine = new BigDecimal(100);
        BorrowingDTO borrowingDTO = BorrowingDTOFactory.createBorrowing(id,
                bookId,
                borrowerId,
                borrowDate,
                expectedReturnDate,
                false,
                null,
                lateReturnWeeklyFine,
                false);
        when(borrowingFacade.findById(id)).thenReturn(borrowingDTO);

        // Act
        String responseJson = mockMvc.perform(get("/api/borrowings/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        BorrowingDTO response = ObjectConverter.convertJsonToObject(responseJson, BorrowingDTO.class);

        // Assert

        assertThat(responseJson).isEqualTo(String.format("{\"id\":1,\"bookId\":%d,\"borrowerId\":%d,\"borrowDate\":\"%s\",\"expectedReturnDate\":\"%s\",\"returned\":%b,\"returnDate\":%s,\"lateReturnWeeklyFine\":100,\"fineResolved\":false}", bookId, borrowerId, borrowDate, expectedReturnDate, false, null));
        assertThat(response).isEqualTo(borrowingDTO);
    }

    @Test
    void updateBorrowing_invalidId_returnsNotFound() throws Exception {
        // Arrange
        Long id = 10L;
        when(borrowingFacade.findById(id)).thenThrow(new ResourceNotFoundException(String.format("Borrowing with id: %d not found", id)));

        // Act and Assert
        String responseJson = mockMvc.perform(get("/api/borrowings/{id}", id)
                        // Assert
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        ApiError error = ObjectConverter.convertJsonToObject(responseJson, ApiError.class);

        assertThat(error.getMessage()).isEqualTo(String.format("Borrowing with id: %d not found", id));
    }
}
