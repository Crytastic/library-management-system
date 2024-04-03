package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookFacade {

    private final BookService bookService;

    @Autowired
    public BookFacade(BookService bookService) {
        this.bookService = bookService;
    }
}
