package cz.muni.fi.pa165.data.repository;

import cz.muni.fi.pa165.data.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.model.BookStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Long bookId;

    @BeforeEach
    void setUp() {
        Book book = new Book(
                "The Lord of the Rings",
                "J. R. R. Tolkien",
                "The Lord of the Rings is an epic high fantasy novel by the English author " +
                        "and scholar J. R. R. Tolkien. Set in Middle-earth, the story began as a sequel to Tolkien's " +
                        "1937 children's book The Hobbit, but eventually developed into a much larger work.",
                BookStatus.AVAILABLE
        );

        Book persistedBook = testEntityManager.persist(book);
        bookId = persistedBook.getId();
    }

    @Test
    void updateById_idFound_returnsOneMoreMore() {
        int updatedCount = bookRepository.updateById(bookId, null, null, null, BookStatus.RESERVED);
        assertThat(updatedCount).isEqualTo(1);
    }

    @Test
    void updateById_idNotFound_returnsZero() {
        int updatedCount = bookRepository.updateById(bookId - 1, null, null, null, BookStatus.RESERVED);
        assertThat(updatedCount).isEqualTo(0);
    }

    @Test
    void findByFilter_someBookFound_returnsListWithSizeOne() {
        List<Book> booksMatchesFilter = bookRepository.findByFilter(null, "J. R. R. Tolkien", null, BookStatus.AVAILABLE);
        assertThat(booksMatchesFilter).hasSize(1);
        Book book = booksMatchesFilter.getFirst();
        assertThat(book.getTitle()).isEqualTo("The Lord of the Rings");
        assertThat(book.getAuthor()).isEqualTo("J. R. R. Tolkien");
        assertThat(book.getStatus()).isEqualTo(BookStatus.AVAILABLE);
        assertThat(book.getDescription()).isEqualTo("The Lord of the Rings is an epic high fantasy novel by the English author " +
                "and scholar J. R. R. Tolkien. Set in Middle-earth, the story began as a sequel to Tolkien's " +
                "1937 children's book The Hobbit, but eventually developed into a much larger work.");
    }

    @Test
    void findByFilter_bookNotFound_returnsEmptyList() {
        List<Book> booksMatchesFilter = bookRepository.findByFilter(null, "J. R. R. Tolkien", null, BookStatus.RESERVED);
        assertThat(booksMatchesFilter).hasSize(0);
    }

}