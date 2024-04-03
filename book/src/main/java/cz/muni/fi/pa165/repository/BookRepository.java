package cz.muni.fi.pa165.repository;

import cz.muni.fi.pa165.dao.BookDAO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;



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
}
