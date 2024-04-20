package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.data.model.Book;
import cz.muni.fi.pa165.data.repository.BookRepository;
import cz.muni.fi.pa165.util.ObjectConverter;
import org.junit.jupiter.api.Test;
import org.openapitools.model.BookDTO;
import org.openapitools.model.BookStatus;
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
public class BookRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void getBook_validId_returnsBook() throws Exception {
        // Arrange
        Long id = 1L;
        String title = "The Lord of the Rings";
        String description = "Fantasy novel";
        String author = "J.R.R. Tolkien";
        BookStatus status = BookStatus.AVAILABLE;
        Book book = new Book(title, author, description, status);
        book.setId(id);
        bookRepository.save(book);

        // Act
        String responseJson = mockMvc.perform(get("/api/books/{id}", id)
                        // Assert
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        BookDTO response = ObjectConverter.convertJsonToObject(responseJson, BookDTO.class);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getAuthor()).isEqualTo(author);
        assertThat(response.getTitle()).isEqualTo(title);
        assertThat(response.getDescription()).isEqualTo(description);
        assertThat(response.getStatus()).isEqualTo(status);
    }

    @Test
    void getBook_invalidId_returnsNotFound() throws Exception {
        // Arrange
        Long id = 10L;

        // Act and Assert
        mockMvc.perform(get("/api/books/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

}
