package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.dao.BookDAO;
import cz.muni.fi.pa165.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.BookDTO;
import org.openapitools.model.BookStatus;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookFacadeTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookFacade bookFacade;

    @Test
    void createBook_validParameters_callsCreateOnBookService() {
        // Arrange
        String title = "The Lord of the Rings";
        String author = "J.R.R. Tolkien";
        String description = "Fantasy";
        BookDAO bookDAO = new BookDAO(title, author, description, BookStatus.AVAILABLE);
        when(bookService.createBook(title, author, description)).thenReturn(bookDAO);

        // Act
        BookDTO result = bookFacade.createBook(title, author, description);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getAuthor()).isEqualTo(author);
        assertThat(result.getDescription()).isEqualTo(description);
        verify(bookService, times(1)).createBook(title, author, description);
    }

    @Test
    void findByFilter_validParameters_returnsListOfBooks() {
        // Arrange
        String title = "The Lord of the Rings";
        String author = "J.R.R. Tolkien";
        String description = "Fantasy";
        BookStatus status = BookStatus.AVAILABLE;
        List<BookDAO> books = new ArrayList<>();
        books.add(new BookDAO(title, author, description, status));
        when(bookService.findByFilter(title, author, description, status)).thenReturn(books);

        // Act
        List<BookDTO> result = bookFacade.findByFilter(title, author, description, status);

        // Assert
        assertThat(result).hasSize(1);
        verify(bookService, times(1)).findByFilter(title, author, description, status);
    }
}
