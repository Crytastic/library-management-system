package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.data.model.Book;
import cz.muni.fi.pa165.exceptionhandling.exceptions.ResourceNotFoundException;
import cz.muni.fi.pa165.mappers.BookMapper;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookFacadeTest {

    @Mock
    private BookService bookService;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookFacade bookFacade;

    @Test
    void createBook_validParameters_callsCreateOnBookService() {
        // Arrange
        String title = "The Lord of the Rings";
        String author = "J.R.R. Tolkien";
        String description = "Fantasy";
        BookStatus status = BookStatus.AVAILABLE;
        Book book = new Book(title, author, description, BookStatus.AVAILABLE);
        BookDTO bookDTO = new BookDTO().author(author).title(title).description(description).status(status);

        when(bookService.createBook(title, author, description)).thenReturn(book);
        when(bookMapper.mapToDto(book)).thenReturn(bookDTO);

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

        List<Book> books = new ArrayList<>();
        books.add(new Book(title, author, description, status));

        List<BookDTO> booksDTO = new ArrayList<>();
        booksDTO.add(new BookDTO().author(author).title(title).description(description).status(status));

        when(bookService.findByFilter(title, author, description, status)).thenReturn(books);
        when(bookMapper.mapToList(books)).thenReturn(booksDTO);


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
        String title = "The Lord of the Rings";
        String author = "J.R.R. Tolkien";
        String description = "Fantasy";
        BookStatus status = BookStatus.AVAILABLE;
        Book book = new Book(title, author, description, status);
        BookDTO bookDTO = new BookDTO().author(author).title(title).description(description).status(status);
        book.setId(id);
        when(bookService.findById(id)).thenReturn(book);
        when(bookMapper.mapToDto(book)).thenReturn(bookDTO);
        // Act
        BookDTO result = bookFacade.findById(id);

        // Assert
        assertThat(result).isEqualTo(bookDTO);
        verify(bookService, times(1)).findById(id);
    }

    @Test
    void findById_bookDoesNotExist_returnsEmptyOptional() {
        // Arrange
        Long id = 1L;
        when(bookService.findById(id)).thenThrow(new ResourceNotFoundException(String.format("Book with id: %d not found", id)));

        // Act + Assert
        Throwable exception = assertThrows(ResourceNotFoundException.class, () -> bookFacade.findById(id));
        assertThat(exception.getMessage()).isEqualTo(String.format("Book with id: %d not found", id));
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
    void deleteAll_allBooksDelete_bookServiceCallsOneTime() {
        // Act
        bookFacade.deleteAll();

        // Assert
        verify(bookService, times(1)).deleteAll();
    }

    @Test
    void updateById_validParameters_returnsUpdatedBook() {
        // Arrange
        Long id = 1L;
        String title = "The Lord of the Rings";
        String author = "J.R.R. Tolkien";
        String description = "Fantasy";
        BookStatus status = BookStatus.BORROWED;
        Book book = new Book();
        book.setId(id);
        book.setDescription(description);
        book.setStatus(status);
        book.setTitle(title);
        book.setAuthor(author);
        BookDTO bookDTO = new BookDTO().id(id).author(author).status(status).title(title).description(description);

        when(bookService.updateById(id, title, author, description, status)).thenReturn(book);
        when(bookMapper.mapToDto(book)).thenReturn(bookDTO);

        // Act
        BookDTO result = bookFacade.updateById(id, title, author, description, status);

        // Assert
        assertThat(result).isEqualTo(bookDTO);
        verify(bookService, times(1)).updateById(id, title, author, description, status);
    }

    @Test
    void updateById_invalidId_throwsResourceNotFoundException() {
        // Arrange
        Long id = 1L;
        String title = "The Lord of the Rings";
        String author = "J.R.R. Tolkien";
        String description = "Fantasy";
        BookStatus status = BookStatus.BORROWED;
        when(bookService.updateById(id, title, author, description, status)).thenThrow(new ResourceNotFoundException(String.format("Book with id: %d not found", id)));

        // Act
        Throwable throwable = assertThrows(ResourceNotFoundException.class, () -> bookFacade.updateById(id, title, author, description, status));

        // Assert
        assertThat(throwable.getMessage()).isEqualTo(String.format("Book with id: %d not found", id));
        verify(bookService, times(1)).updateById(id, title, author, description, status);
    }

}
