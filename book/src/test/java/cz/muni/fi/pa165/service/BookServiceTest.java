package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.dao.BookDAO;
import cz.muni.fi.pa165.repository.BookRepository;
import cz.muni.fi.pa165.stubs.RentalServiceStub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openapitools.model.BookStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

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
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(foundBook));

        // Act
        Optional<BookDAO> result = bookService.findById(1L);

        // Assert
        assertThat(result).isPresent().contains(foundBook);
    }

    @Test
    void findById_bookNotFound_returnsEmpty() {
        // Arrange
        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.empty());

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
        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Optional<BookDAO> result = bookService.updateById(id, newTitle, newAuthor, newDescription, newStatus);

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void findBookRentals_bookFound_returnsRentals() {
        // Arrange
        Long id = 1L;
        List<String> rentals = List.of("Rental 1", "Rental 2");
        Mockito.when(rentalServiceStub.apiCallToRentalServiceToFindBookRentals(id)).thenReturn(rentals);
        Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(new BookDAO("", "", "", BookStatus.AVAILABLE)));

        // Act
        Optional<List<String>> result = bookService.findBookRentals(id);

        // Assert
        assertThat(result).isPresent().contains(rentals);
    }
}
