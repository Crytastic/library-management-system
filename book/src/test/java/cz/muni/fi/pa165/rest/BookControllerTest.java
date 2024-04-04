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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Test
    void getBook_validId_returnsBook() {
        // Arrange
        Long id = 1L;
        String title = "The Lord of the Rings";
        String description = "Fantasy novel";
        String author = "J.R.R. Tolkien";
        BookStatus status = BookStatus.AVAILABLE;
        BookDTO book = BookDTOFactory.createBook(title, description, author, status);
        when(bookFacade.findById(id)).thenReturn(Optional.of(book));

        // Act
        ResponseEntity<BookDTO> response = bookController.getBook(id);

        // Assert
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(book);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getBook_invalidId_returnsNotFound() {
        // Arrange
        Long id = 1L;
        when(bookFacade.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<BookDTO> response = bookController.getBook(id);

        // Assert
        assertThat(response.getBody()).isNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getBookRentals_validId_returnsRentals() {
        // Arrange
        Long id = 1L;
        List<String> rentals = new ArrayList<>();
        rentals.add("Rental 1");
        rentals.add("Rental 2");
        when(bookFacade.findBookRentals(id)).thenReturn(Optional.of(rentals));

        // Act
        ResponseEntity<List<String>> response = bookController.getBookRentals(id);

        // Assert
        assertThat(response.getBody()).isEqualTo(rentals);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getBookRentals_invalidId_returnsNotFound() {
        // Arrange
        Long id = 1L;
        when(bookFacade.findBookRentals(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<List<String>> response = bookController.getBookRentals(id);

        // Assert
        assertThat(response.getBody()).isNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateBook_validIdAndRequestBody_updatesBook() {
        // Arrange
        Long id = 1L;
        String title = "The Lord of the Rings";
        String author = "Tolkien";
        String description = "Fantasy novel";
        BookStatus status = BookStatus.AVAILABLE;
        BookDTO book = BookDTOFactory.createBook(title, author, description, status);

        String newTitle = "The Lord of the Rings: The Two Towers";
        String newAuthor = "J. R. R. Tolkien";
        String newDescription = "The Lord of the Rings is an epic high fantasy novel by the English author and scholar J. R. R. Tolkien. Set in Middle-earth, the story began as a sequel to Tolkien's 1937 children's book The Hobbit, but eventually developed into a much larger work.";
        BookStatus newStatus = BookStatus.RENTED;
        BookDTO updatedBook = BookDTOFactory.createBook(newTitle, newAuthor, newDescription, newStatus);

        when(bookFacade.updateById(id, newTitle, newAuthor, newDescription, newStatus)).thenReturn(Optional.of(updatedBook));

        // Act
        ResponseEntity<BookDTO> response = bookController.updateBook(id, newTitle, newAuthor, newDescription, newStatus);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo(newTitle);
        assertThat(response.getBody().getAuthor()).isEqualTo(newAuthor);
        assertThat(response.getBody().getDescription()).isEqualTo(newDescription);
        assertThat(response.getBody().getStatus()).isEqualTo(newStatus);
    }

    @Test
    void updateBook_invalidId_returnsNotFound() {
        // Arrange
        Long id = 1L;
        String title = "The Lord of the Rings";
        String description = "Fantasy novel";
        String author = "Tolkien";
        BookStatus status = BookStatus.AVAILABLE;
        when(bookFacade.updateById(id, title, author, description, status)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<BookDTO> response = bookController.updateBook(id, title, author, description, status);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }
}