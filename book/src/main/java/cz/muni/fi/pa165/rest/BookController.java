package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.BookFacade;
import org.openapitools.api.BookApi;
import org.openapitools.model.BookTestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController implements BookApi {

    private final BookFacade bookFacade;

    @Autowired
    public BookController(BookFacade bookFacade) {
        this.bookFacade = bookFacade;
    }

    @Override
    public ResponseEntity<BookTestResponse> test() {
        return new ResponseEntity<>(new BookTestResponse().message("Book microservice is ready"), HttpStatus.OK);
    }
}
