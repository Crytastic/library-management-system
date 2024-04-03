package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.dao.BookDAO;
import cz.muni.fi.pa165.service.BookService;
import org.openapitools.model.Book;
import org.openapitools.model.BookStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookFacade {

    private final BookService bookService;

    @Autowired
    public BookFacade(BookService bookService) {
        this.bookService = bookService;
    }

    public List<Book> findAll() {
        return bookService
                .findAll()
                .stream()
                .map(dao -> new Book()
                        .author(dao.getAuthor())
                        .description(dao.getDescription())
                        .status(dao.getStatus())
                        .title(dao.getTitle()))
                .collect(Collectors.toList());
    }

    public Book createBook(String title, String description, String author) {
        BookDAO bookDAO = bookService.createBook(title, description, author);
        return new Book()
                .title(bookDAO.getTitle())
                .description(bookDAO.getDescription())
                .author(bookDAO.getAuthor())
                .status(bookDAO.getStatus());
    }
}
