package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.Book;
import cz.muni.fi.pa165.data.repository.BookRepository;
import cz.muni.fi.pa165.exceptionhandling.exceptions.ResourceNotFoundException;
import cz.muni.fi.pa165.stubs.RentalServiceStub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.BookStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    RentalServiceStub rentalServiceStub;

    @InjectMocks
    private BookService bookService;

    @Test
    void findById_bookFound_returnsBook() {
        // Arrange
        Book foundBook = new Book("The Lord of the Rings", "J. R. R. Tolkien", "The Lord of the Rings is an epic high fantasy novel by the English author and scholar J. R. R. Tolkien. Set in Middle-earth, the story began as a sequel to Tolkien's 1937 children's book The Hobbit, but eventually developed into a much larger work.", BookStatus.AVAILABLE);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(foundBook));

        // Act
        Book result = bookService.findById(1L);

        // Assert
        assertThat(result).isEqualTo(foundBook);
    }

    @Test
    void findById_bookNotFound_throwsResourceNotFoundException() {
        // Arrange
        when(bookRepository.findById(1L)).thenThrow(new ResourceNotFoundException(String.format("Book with id: %d not found",1L)));

        // Act + Assert
        Throwable exception = assertThrows(ResourceNotFoundException.class, () -> bookRepository.findById(1L));
        assertThat(exception.getMessage()).isEqualTo("Book with id: %d not found",1L);
    }

    @Test
    void updateById_bookNotFound_returnsZero() {
        // Arrange
        Long id = 1L;
        String newTitle = "The Lord of the Rings: The Two Towers";
        String newAuthor = "J. R. R. Tolkien";
        String newDescription = "The Lord of the Rings is an epic high fantasy novel by the English author and scholar J. R. R. Tolkien. Set in Middle-earth, the story began as a sequel to Tolkien's 1937 children's book The Hobbit, but eventually developed into a much larger work.";
        BookStatus newStatus = BookStatus.RENTED;
        when(bookRepository.updateById(id, newTitle, newAuthor, newDescription, newStatus)).thenReturn(0);

        // Act
        int result = bookService.updateById(id, newTitle, newAuthor, newDescription, newStatus);

        // Assert
        assertThat(result).isEqualTo(0);
    }

    @Test
    void updateById_bookFound_returnsOneOrMore() {
        // Arrange
        Long id = 1L;
        String newTitle = "The Lord of the Rings: The Two Towers";
        String newAuthor = "J. R. R. Tolkien";
        String newDescription = "The Lord of the Rings is an epic high fantasy novel by the English author and scholar J. R. R. Tolkien. Set in Middle-earth, the story began as a sequel to Tolkien's 1937 children's book The Hobbit, but eventually developed into a much larger work.";
        BookStatus newStatus = BookStatus.RENTED;
        when(bookRepository.updateById(id, newTitle, newAuthor, newDescription, newStatus)).thenReturn(1);

        // Act
        int result = bookService.updateById(id, newTitle, newAuthor, newDescription, newStatus);

        // Assert
        assertThat(result).isEqualTo(1);
    }

    @Test
    void findBookRentals_bookFound_returnsRentals() {
        // Arrange
        Long id = 1L;
        List<String> rentals = List.of("Rental 1", "Rental 2");
        when(rentalServiceStub.apiCallToRentalServiceToFindBookRentals(id)).thenReturn(rentals);
        when(bookRepository.findById(id)).thenReturn(Optional.of(new Book("", "", "", BookStatus.AVAILABLE)));

        // Act
        Optional<List<String>> result = bookService.findBookRentals(id);

        // Assert
        assertThat(result).isPresent().contains(rentals);
    }

    @Test
    void findBookRentals_bookNotFound_returnsEmpty() {
        // Arrange
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<List<String>> result = bookService.findBookRentals(id);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void findByFilter_authorExists_returnsBooks() {
        // Arrange
        String author = "J. R. R. Tolkien";
        Book book1 = new Book("The Lord of the Rings: The Fellowship of the Ring", author, "Fantasy novel", BookStatus.AVAILABLE);
        Book book2 = new Book("The Hobbit", author, "Fantasy novel", BookStatus.AVAILABLE);
        List<Book> booksByAuthor = List.of(book1, book2);
        when(bookRepository.findByFilter(null, author, null, null)).thenReturn(booksByAuthor);

        // Act
        List<Book> result = bookService.findByFilter(null, author, null, null);

        // Assert
        assertThat(result).isNotNull().hasSize(2).containsExactlyInAnyOrder(book1, book2);
    }

    @Test
    void findByFilter_authorDoesNotExist_returnsEmptyList() {
        // Arrange
        String author = "J. K. Rowling";
        when(bookRepository.findByFilter(null, author, null, null)).thenReturn(new ArrayList<>());

        // Act
        List<Book> result = bookService.findByFilter(null, author, null, null);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void findByFilter_booksAvailable_returnsAvailableBooks() {
        // Arrange
        String author = "J. R. R. Tolkien";
        Book book1 = new Book("The Lord of the Rings: The Fellowship of the Ring", author, "Fantasy novel", BookStatus.AVAILABLE);
        Book book2 = new Book("The Hobbit", author, "Fantasy novel", BookStatus.AVAILABLE);
        List<Book> availableBooks = List.of(book1, book2);
        when(bookRepository.findByFilter(null, null, null, BookStatus.AVAILABLE)).thenReturn(availableBooks);

        // Act
        List<Book> result = bookService.findByFilter(null, null, null, BookStatus.AVAILABLE);

        // Assert
        assertThat(result).isNotNull().hasSize(2).containsExactlyInAnyOrder(book1, book2);
    }

    @Test
    void findByFilter_noBooksAvailable_returnsEmptyList() {
        // Arrange
        when(bookRepository.findByFilter(null, null, null, BookStatus.AVAILABLE)).thenReturn(new ArrayList<>());

        // Act
        List<Book> result = bookService.findByFilter(null, null, null, BookStatus.AVAILABLE);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void findByFilter_anyBooksExist_returnsAllBooks() {
        // Arrange
        String author = "J. R. R. Tolkien";
        Book book1 = new Book("The Lord of the Rings: The Fellowship of the Ring", author, "Fantasy novel", BookStatus.AVAILABLE);
        Book book2 = new Book("The Hobbit", author, "Fantasy novel", BookStatus.AVAILABLE);
        List<Book> allBooks = List.of(book1, book2);
        when(bookRepository.findByFilter(null, null, null, null)).thenReturn(allBooks);

        // Act
        List<Book> result = bookService.findByFilter(null, null, null, null);

        // Assert
        assertThat(result).isNotNull().hasSize(2).containsExactlyInAnyOrder(book1, book2);
    }

    @Test
    void deleteById_singleBookDelete_callsBookRepositoryDelete() {
        // Arrange
        Long idToDelete = 1L;

        // Act
        bookService.deleteById(idToDelete);

        // Assert
        verify(bookRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void createBook_returnsCreatedBook() {
        // Arrange
        String title = "The Lord of the Rings";
        String description = "Fantasy novel";
        String author = "J.R.R. Tolkien";
        Book createdBook = new Book(title, author, description, BookStatus.AVAILABLE);
        when(bookRepository.save(createdBook)).thenReturn(createdBook);

        // Act
        Book result = bookService.createBook(title, author, description);

        // Assert
        assertThat(result).isNotNull().isEqualTo(createdBook);
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getAuthor()).isEqualTo(author);
        assertThat(result.getDescription()).isEqualTo(description);
        verify(bookRepository, times(1)).save(createdBook);
    }
}
