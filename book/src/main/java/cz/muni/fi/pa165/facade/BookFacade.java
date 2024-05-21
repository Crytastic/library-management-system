package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.mappers.BookMapper;
import cz.muni.fi.pa165.service.BookService;
import org.openapitools.model.BookDTO;
import org.openapitools.model.BookStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Facade layer for managing books.
 * Provides methods for interacting with books.
 *
 * @author Martin Suchánek
 */
@Service
public class BookFacade {
    private final BookService bookService;

    private final BookMapper bookMapper;

    @Autowired
    public BookFacade(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    public List<BookDTO> findByFilter(String title, String author, String description, BookStatus status) {
        return bookMapper.mapToList(bookService.findByFilter(title, author, description, status));
    }

    public BookDTO createBook(String title, String author, String description) {
        return bookMapper.mapToDto(bookService.createBook(title, author, description));
    }

    public BookDTO findById(Long id) {
        return bookMapper.mapToDto(bookService.findById(id));
    }

    public void deleteById(Long id) {
        bookService.deleteById(id);
    }

    public BookDTO updateById(Long id, String title, String author, String description, BookStatus status) {
        return bookMapper.mapToDto(bookService.updateById(id, title, author, description, status));
    }

    public void deleteAll() {
        bookService.deleteAll();
    }
}
