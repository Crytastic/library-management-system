package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.BookFacade;
import cz.muni.fi.pa165.util.BookDTOFactory;
import cz.muni.fi.pa165.util.ObjectConverter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openapitools.model.BookDTO;
import org.openapitools.model.BookStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {BookRestController.class})
public class BookRestControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookFacade bookFacade;

    @Test
    void getBook_validId_returnsBook() throws Exception {
        // Arrange
        Long id = 1L;
        String title = "The Lord of the Rings";
        String description = "Fantasy novel";
        String author = "J.R.R. Tolkien";
        BookStatus status = BookStatus.AVAILABLE;
        BookDTO bookDTO = BookDTOFactory.createBook(id, title, author, description, status);
        Mockito.when(bookFacade.findById(id)).thenReturn(Optional.of(bookDTO));

        // Act
        String responseJson = mockMvc.perform(get("/api/books/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        BookDTO response = ObjectConverter.convertJsonToObject(responseJson, BookDTO.class);

        // Assert
        assertThat(responseJson).isEqualTo("{\"id\":1,\"title\":\"The Lord of the Rings\",\"author\":\"J.R.R. Tolkien\",\"description\":\"Fantasy novel\",\"status\":\"AVAILABLE\"}");

        assertThat(response).isEqualTo(bookDTO);
    }

    @Test
    void getBook_invalidId_returnsNotFound() throws Exception {
        // Arrange
        Long id = 10L;
        Mockito.when(bookFacade.findById(id)).thenReturn(Optional.empty());
        // Act and Assert
        String responseJson = mockMvc.perform(get("/api/books/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(responseJson).isEqualTo("");
    }

}
