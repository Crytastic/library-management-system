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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookRestControllerTest {

    @Mock
    private BookFacade bookFacade;

    @InjectMocks
    private BookRestController bookRestController;

    @Test
    void createBook_validRequestBody_createsBook() {
        // Arrange
        Long id = 1L;
        String title = "The Lord of the Rings";
        String description = "Fantasy novel";
        String author = "J.R.R. Tolkien";
        BookStatus status = BookStatus.AVAILABLE;
        BookDTO createdBook = BookDTOFactory.createBook(id, title, description, author, status);
        when(bookFacade.createBook(title, description, author)).thenReturn(createdBook);

        // Act
        ResponseEntity<BookDTO> response = bookRestController.createBook(title, description, author);

        // Assert
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(createdBook);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void deleteBook_validId_deletesBook() {
        // Arrange
        Long id = 1L;
        doNothing().when(bookFacade).deleteById(anyLong());

        // Act
        ResponseEntity<Void> response = bookRestController.deleteBook(id);

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
        BookDTO book = BookDTOFactory.createBook(id, title, description, author, status);
        when(bookFacade.findById(id)).thenReturn(book);

        // Act
        ResponseEntity<BookDTO> response = bookRestController.getBook(id);

        // Assert
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(book);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getBookBorrowings_validId_returnsBorrowings() {
        // Arrange
        Long id = 1L;
        List<String> borrowings = new ArrayList<>();
        borrowings.add("Borrowing 1");
        borrowings.add("Borrowing 2");
        when(bookFacade.findBookBorrowings(id)).thenReturn(borrowings);

        // Act
        ResponseEntity<List<String>> response = bookRestController.getBookBorrowings(id);

        // Assert
        assertThat(response.getBody()).isEqualTo(borrowings);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void updateBook_validIdAndRequestBody_returnsOk() {
        // Arrange
        Long id = 1L;
        String newTitle = "The Lord of the Rings: The Two Towers";
        String newAuthor = "J. R. R. Tolkien";
        String newDescription = "The Lord of the Rings is an epic high fantasy novel by the English author and scholar J. R. R. Tolkien. Set in Middle-earth, the story began as a sequel to Tolkien's 1937 children's book The Hobbit, but eventually developed into a much larger work.";
        BookStatus newStatus = BookStatus.BORROWED;
        BookDTO bookDTO = new BookDTO().id(id).title(newTitle).author(newAuthor).description(newDescription).status(newStatus);

        when(bookFacade.updateById(id, newTitle, newAuthor, newDescription, newStatus)).thenReturn(bookDTO);

        // Act
        ResponseEntity<BookDTO> response = bookRestController.updateBook(id, newTitle, newAuthor, newDescription, newStatus);

        // Assert
        assertThat(response.getBody()).isEqualTo(bookDTO);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getBooks_validParameters_returnsBooks() {
        // Arrange
        Long id = 1L;
        String title = "The Lord of the Rings";
        String author = "Tolkien";
        String description = "Fantasy novel";
        BookStatus status = BookStatus.AVAILABLE;
        List<BookDTO> books = new ArrayList<>();
        books.add(BookDTOFactory.createBook(id, title, author, description, status));
        when(bookFacade.findByFilter(title, author, description, status)).thenReturn(books);

        // Act
        ResponseEntity<List<BookDTO>> response = bookRestController.getBooks(title, author, description, status);

        // Assert
        assertThat(response.getBody()).isEqualTo(books);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
