package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.BookFacade;
import org.openapitools.api.BookApi;
import org.openapitools.model.BookDTO;
import org.openapitools.model.BookStatus;
import org.openapitools.model.BookTestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller layer for managing books.
 * Handles HTTP requests related to books.
 *
 * @author Martin Suchánek
 */
@RestController
public class BookRestController implements BookApi {

    private final BookFacade bookFacade;

    @Autowired
    public BookRestController(BookFacade bookFacade) {
        this.bookFacade = bookFacade;
    }

    @Override
    public ResponseEntity<BookDTO> createBook(String title, String description, String author) {
        return new ResponseEntity<>(bookFacade.createBook(title, description, author), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteBook(Long id) {
        bookFacade.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<BookDTO> getBook(Long id) {
        return new ResponseEntity<>(bookFacade.findById(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<String>> getBookRentals(Long id) {
        return new ResponseEntity<>(bookFacade.findBookRentals(id), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<BookDTO>> getBooks(String title, String author, String description, BookStatus status) {
        return new ResponseEntity<>(bookFacade.findByFilter(title, author, description, status), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BookTestResponse> test() {
        return new ResponseEntity<>(new BookTestResponse().message("Book microservice is ready"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Void> updateBook(Long id, String title, String author, String description, BookStatus status) {
        int modifiedCount = bookFacade.updateById(id, title, author, description, status);
        return new ResponseEntity<>(modifiedCount > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
    }
}
