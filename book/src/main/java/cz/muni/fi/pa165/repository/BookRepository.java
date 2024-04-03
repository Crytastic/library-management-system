package cz.muni.fi.pa165.repository;

import cz.muni.fi.pa165.dao.BookDAO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Repository
public class BookRepository {
    private final HashMap<Long, BookDAO> books = new HashMap<>();

    private static Long index = 1L;

    public List<BookDAO> findAll() {
        return books.values().stream().toList();
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
}
