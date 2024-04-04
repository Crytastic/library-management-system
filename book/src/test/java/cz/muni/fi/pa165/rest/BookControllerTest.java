package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.BookFacade;
import cz.muni.fi.pa165.util.BookDTOFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.BookDTO;
import org.openapitools.model.BookStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @Mock
    private BookFacade bookFacade;

    @InjectMocks
    private BookController bookController;

    @Test
    void createBook_validRequestBody_createsBook() {
        // Arrange
        String title = "The Lord of the Rings";
        String description = "Fantasy novel";
        String author = "J.R.R. Tolkien";
        BookStatus status = BookStatus.AVAILABLE;
        BookDTO createdBook = BookDTOFactory.createBook(title, description, author, status);
        when(bookFacade.createBook(title, description, author)).thenReturn(createdBook);

        // Act
        ResponseEntity<BookDTO> response = bookController.createBook(title, description, author);

        // Assert
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(createdBook);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void deleteBook_validId_deletesBook() {
        // Arrange
        Long id = 1L;

        // Act
        ResponseEntity<Void> response = bookController.deleteBook(id);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(bookFacade, times(1)).deleteById(id);
    }
}