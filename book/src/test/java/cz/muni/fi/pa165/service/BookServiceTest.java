package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.dao.BookDAO;
import cz.muni.fi.pa165.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.BookStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

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
}
