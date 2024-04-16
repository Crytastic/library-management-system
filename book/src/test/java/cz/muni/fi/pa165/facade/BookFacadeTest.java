package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.dao.BookDAO;
import cz.muni.fi.pa165.service.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.BookDTO;
import org.openapitools.model.BookStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Test
    void findById_bookExists_returnsBookDTO() {
        // Arrange
        Long id = 1L;
        BookDAO bookDAO = new BookDAO("The Lord of the Rings", "J.R.R. Tolkien", "Fantasy", BookStatus.AVAILABLE);
        when(bookService.findById(id)).thenReturn(Optional.of(bookDAO));

        // Act
        Optional<BookDTO> result = bookFacade.findById(id);

        // Assert
        assertThat(result).isPresent();
        verify(bookService, times(1)).findById(id);
    }

    @Test
    void findById_bookDoesNotExist_returnsEmptyOptional() {
        // Arrange
        Long id = 1L;
        when(bookService.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<BookDTO> result = bookFacade.findById(id);

        // Assert
        assertThat(result).isEmpty();
        verify(bookService, times(1)).findById(id);
    }

    @Test
    void deleteById_validId_deletesBook() {
        // Arrange
        Long id = 1L;

        // Act
        bookFacade.deleteById(id);

        // Assert
        verify(bookService, times(1)).deleteById(id);
    }

    @Test
    void updateById_validParameters_returnsUpdatedBookDTO() {
        // Arrange
        Long id = 1L;
        String title = "The Lord of the Rings";
        String author = "J.R.R. Tolkien";
        String description = "Fantasy";
        BookStatus status = BookStatus.RENTED;
        BookDAO updatedBookDAO = new BookDAO(title, author, description, status);
        when(bookService.updateById(id, title, author, description, status)).thenReturn(Optional.of(updatedBookDAO));

        // Act
        Optional<BookDTO> result = bookFacade.updateById(id, title, author, description, status);

        // Assert
        assertThat(result).isPresent();
        verify(bookService, times(1)).updateById(id, title, author, description, status);
    }

    @Test
    void updateById_invalidId_returnsEmptyOptional() {
        // Arrange
        Long id = 1L;
        String title = "The Lord of the Rings";
        String author = "J.R.R. Tolkien";
        String description = "Fantasy";
        BookStatus status = BookStatus.RENTED;
        when(bookService.updateById(id, title, author, description, status)).thenReturn(Optional.empty());

        // Act
        Optional<BookDTO> result = bookFacade.updateById(id, title, author, description, status);

        // Assert
        assertThat(result).isEmpty();
        verify(bookService, times(1)).updateById(id, title, author, description, status);
    }

    @Test
    void findBookRentals_validId_returnsListOfRentals() {
        // Arrange
        Long id = 1L;
        List<String> rentals = new ArrayList<>();
        rentals.add("Pepa z Depa");
        rentals.add("Miloš Vokuřil");
        when(bookService.findBookRentals(id)).thenReturn(Optional.of(rentals));

        // Act
        Optional<List<String>> result = bookFacade.findBookRentals(id);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).hasSize(2);
        verify(bookService, times(1)).findBookRentals(id);
    }

    @Test
    void findBookRentals_invalidId_returnsEmptyOptional() {
        // Arrange
        Long id = 1L;
        when(bookService.findBookRentals(id)).thenReturn(Optional.empty());

        // Act
        Optional<List<String>> result = bookFacade.findBookRentals(id);

        // Assert
        assertThat(result).isEmpty();
        verify(bookService, times(1)).findBookRentals(id);
    }
}
