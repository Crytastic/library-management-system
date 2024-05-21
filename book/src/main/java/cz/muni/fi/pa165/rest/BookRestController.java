package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.BookFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.openapitools.api.BookApi;
import org.openapitools.model.BookDTO;
import org.openapitools.model.BookStatus;
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
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<BookDTO> createBook(String title, String description, String author) {
        return new ResponseEntity<>(bookFacade.createBook(title, description, author), HttpStatus.CREATED);
    }

    @Override
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteBook(Long id) {
        bookFacade.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<BookDTO> getBook(Long id) {
        return new ResponseEntity<>(bookFacade.findById(id), HttpStatus.OK);
    }

    @Override
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<BookDTO>> getBooks(String title, String author, String description, BookStatus status) {
        return new ResponseEntity<>(bookFacade.findByFilter(title, author, description, status), HttpStatus.OK);
    }

    @Override
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> updateBook(Long id, String title, String author, String description, BookStatus status) {
        bookFacade.updateById(id, title, author, description, status);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteBooks() {
        bookFacade.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
