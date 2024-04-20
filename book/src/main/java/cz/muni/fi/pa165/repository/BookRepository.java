package cz.muni.fi.pa165.repository;

import cz.muni.fi.pa165.data.BookDAO;
import org.openapitools.model.BookStatus;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Repository layer for managing books.
 * Provides methods for storing, retrieving and updating, books.
 *
 * @author Martin Suchánek
 */
@Repository
public class BookRepository {
    private final HashMap<Long, BookDAO> books = new HashMap<>();

    private static Long index = 1L;

    public List<BookDAO> findByFilter(String title, String author, String description, BookStatus status) {
        return books
                .values()
                .stream()
                .filter(book -> (title == null || book.getTitle().equals(title)) &&
                        (author == null || book.getAuthor().equals(author)) &&
                        (description == null || book.getDescription().contains(description.toLowerCase())) &&
                        (status == null || book.getStatus().equals(status)))
                .collect(Collectors.toList());
    }

    public BookDAO store(BookDAO bookDAO) {
        bookDAO.setId(index);
        books.put(bookDAO.getId(), bookDAO);
        index++;
        return bookDAO;
    }

    public Optional<BookDAO> findById(Long id) {
        return Optional.ofNullable(books.get(id));
    }

    public void deleteById(Long id) {
        books.remove(id);
    }

    public Optional<BookDAO> updateById(Long id, BookDAO bookDAO) {
        books.put(id, bookDAO);
        return Optional.ofNullable(bookDAO);
    }
}
