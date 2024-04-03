package cz.muni.fi.pa165.repository;

import cz.muni.fi.pa165.dao.BookDAO;
import org.openapitools.model.Book;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;



@Repository
public class BookRepository {
    private final HashMap<Long, BookDAO> books = new HashMap<>();

    public List<BookDAO> findAll() {
        return books.values().stream().toList();
    }
}
