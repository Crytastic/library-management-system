package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.dao.BookDAO;
import cz.muni.fi.pa165.service.BookService;
import org.openapitools.model.BookDTO;
import org.openapitools.model.BookStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookFacade {

    private final BookService bookService;

    @Autowired
    public BookFacade(BookService bookService) {
        this.bookService = bookService;
    }

    public List<BookDTO> findByFilter(String title, String author, String description, BookStatus status) {
        return bookService
                .findByFilter(title, author, description, status)
                .stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    public BookDTO createBook(String title, String description, String author) {
        BookDAO bookDAO = bookService.createBook(title, description, author);
        return convertToDTO(bookDAO);
    }

    public Optional<BookDTO> findById(Long id) {
        return bookService.findById(id).map(this::convertToDTO);
    }

    public void deleteById(Long id) {
        bookService.deleteById(id);
    }

    public Optional<BookDTO> updateById(Long id, String title, String author, String description, BookStatus status) {
        return bookService.updateById(id, title, author, description, status).map(this::convertToDTO);
    }

    public Optional<List<String>> findBookRentals(Long id) {
        return bookService.findBookRentals(id);
    }

    private BookDTO convertToDTO(BookDAO bookDAO) {
        return new BookDTO()
                .title(bookDAO.getTitle())
                .author(bookDAO.getAuthor())
                .description(bookDAO.getDescription())
                .status(bookDAO.getStatus());
    }
}
