package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.dao.BookDAO;
import cz.muni.fi.pa165.repository.BookRepository;
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
        BookDAO foundBook = new BookDAO("The Lord of the Rings", "J. R. R. Tolkien", "The Lord of the Rings is an epic high fantasy novel by the English author and scholar J. R. R. Tolkien. Set in Middle-earth, the story began as a sequel to Tolkien's 1937 children's book The Hobbit, but eventually developed into a much larger work.", BookStatus.AVAILABLE);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(foundBook));

        // Act
        Optional<BookDAO> result = bookService.findById(1L);

        // Assert
        assertThat(result).isPresent().contains(foundBook);
    }

    @Test
    void findById_bookNotFound_returnsEmpty() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        Optional<BookDAO> result = bookService.findById(1L);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void updateById_bookNotFound_returnsEmpty() {
        // Arrange
        Long id = 1L;
        String newTitle = "The Lord of the Rings: The Two Towers";
        String newAuthor = "J. R. R. Tolkien";
        String newDescription = "The Lord of the Rings is an epic high fantasy novel by the English author and scholar J. R. R. Tolkien. Set in Middle-earth, the story began as a sequel to Tolkien's 1937 children's book The Hobbit, but eventually developed into a much larger work.";
        BookStatus newStatus = BookStatus.RENTED;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<BookDAO> result = bookService.updateById(id, newTitle, newAuthor, newDescription, newStatus);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void updateById_bookFound_updatesBook() {
        // Arrange
        Long id = 1L;
        String newTitle = "The Lord of the Rings: The Two Towers";
        String newAuthor = "J. R. R. Tolkien";
        String newDescription = "The Lord of the Rings is an epic high fantasy novel by the English author and scholar J. R. R. Tolkien. Set in Middle-earth, the story began as a sequel to Tolkien's 1937 children's book The Hobbit, but eventually developed into a much larger work.";
        BookStatus newStatus = BookStatus.RENTED;
        BookDAO existingBook = new BookDAO("The Lord of the Rings", "Tolkien", "Fantasy novel", BookStatus.AVAILABLE);
        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(bookRepository.updateById(id, existingBook)).thenReturn(Optional.of(existingBook));

        // Act
        Optional<BookDAO> result = bookService.updateById(id, newTitle, newAuthor, newDescription, newStatus);

        // Assert
        assertThat(result).isPresent().contains(existingBook);
        assertThat(result.get().getTitle()).isEqualTo(newTitle);
        assertThat(result.get().getAuthor()).isEqualTo(newAuthor);
        assertThat(result.get().getDescription()).isEqualTo(newDescription);
        assertThat(result.get().getStatus()).isEqualTo(newStatus);
    }

    @Test
    void findBookRentals_bookFound_returnsRentals() {
        // Arrange
        Long id = 1L;
        List<String> rentals = List.of("Rental 1", "Rental 2");
        when(rentalServiceStub.apiCallToRentalServiceToFindBookRentals(id)).thenReturn(rentals);
        when(bookRepository.findById(id)).thenReturn(Optional.of(new BookDAO("", "", "", BookStatus.AVAILABLE)));

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
        BookDAO book1 = new BookDAO("The Lord of the Rings: The Fellowship of the Ring", author, "Fantasy novel", BookStatus.AVAILABLE);
        BookDAO book2 = new BookDAO("The Hobbit", author, "Fantasy novel", BookStatus.AVAILABLE);
        List<BookDAO> booksByAuthor = List.of(book1, book2);
        when(bookRepository.findByFilter(null, author, null, null)).thenReturn(booksByAuthor);

        // Act
        List<BookDAO> result = bookService.findByFilter(null, author, null, null);

        // Assert
        assertThat(result).isNotNull().hasSize(2).containsExactlyInAnyOrder(book1, book2);
    }

    @Test
    void findByFilter_authorDoesNotExist_returnsEmptyList() {
        // Arrange
        String author = "J. K. Rowling";
        when(bookRepository.findByFilter(null, author, null, null)).thenReturn(new ArrayList<>());

        // Act
        List<BookDAO> result = bookService.findByFilter(null, author, null, null);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void findByFilter_booksAvailable_returnsAvailableBooks() {
        // Arrange
        String author = "J. R. R. Tolkien";
        BookDAO book1 = new BookDAO("The Lord of the Rings: The Fellowship of the Ring", author, "Fantasy novel", BookStatus.AVAILABLE);
        BookDAO book2 = new BookDAO("The Hobbit", author, "Fantasy novel", BookStatus.AVAILABLE);
        List<BookDAO> availableBooks = List.of(book1, book2);
        when(bookRepository.findByFilter(null, null, null, BookStatus.AVAILABLE)).thenReturn(availableBooks);

        // Act
        List<BookDAO> result = bookService.findByFilter(null, null, null, BookStatus.AVAILABLE);

        // Assert
        assertThat(result).isNotNull().hasSize(2).containsExactlyInAnyOrder(book1, book2);
    }

    @Test
    void findByFilter_noBooksAvailable_returnsEmptyList() {
        // Arrange
        when(bookRepository.findByFilter(null, null, null, BookStatus.AVAILABLE)).thenReturn(new ArrayList<>());

        // Act
        List<BookDAO> result = bookService.findByFilter(null, null, null, BookStatus.AVAILABLE);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void findByFilter_anyBooksExist_returnsAllBooks() {
        // Arrange
        String author = "J. R. R. Tolkien";
        BookDAO book1 = new BookDAO("The Lord of the Rings: The Fellowship of the Ring", author, "Fantasy novel", BookStatus.AVAILABLE);
        BookDAO book2 = new BookDAO("The Hobbit", author, "Fantasy novel", BookStatus.AVAILABLE);
        List<BookDAO> allBooks = List.of(book1, book2);
        when(bookRepository.findByFilter(null, null, null, null)).thenReturn(allBooks);

        // Act
        List<BookDAO> result = bookService.findByFilter(null, null, null, null);

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
        BookDAO createdBook = new BookDAO(title, author, description, BookStatus.AVAILABLE);
        when(bookRepository.store(any(BookDAO.class))).thenReturn(createdBook);

        // Act
        BookDAO result = bookService.createBook(title, description, author);

        // Assert
        assertThat(result).isNotNull().isEqualTo(createdBook);
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getAuthor()).isEqualTo(author);
        assertThat(result.getDescription()).isEqualTo(description);
        verify(bookRepository, times(1)).store(any(BookDAO.class));
    }
}
