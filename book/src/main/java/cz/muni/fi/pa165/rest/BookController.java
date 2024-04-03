package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.BookFacade;
import org.openapitools.api.BookApi;
import org.openapitools.model.Book;
import org.openapitools.model.BookStatus;
import org.openapitools.model.BookTestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController implements BookApi {

    private final BookFacade bookFacade;

    @Autowired
    public BookController(BookFacade bookFacade) {
        this.bookFacade = bookFacade;
    }

    @Override
    public ResponseEntity<Book> createBook(String title, String description, String author) {
        return new ResponseEntity<>(bookFacade.createBook(title, description, author), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteBook(Long id) {
        bookFacade.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Book> getBook(Long id) {
        Optional<Book> book = bookFacade.findById(id);
        return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<List<Book>> getBooks() {
        return new ResponseEntity<>(bookFacade.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookTestResponse> test() {
        return new ResponseEntity<>(new BookTestResponse().message("Book microservice is ready"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Book> updateBook(Long id, String title, String author, String description, BookStatus status) {
        Optional<Book> book = bookFacade.updateById(id, title, author, description, status);
        return book.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
