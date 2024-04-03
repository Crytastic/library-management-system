package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.dao.BookDAO;
import cz.muni.fi.pa165.repository.BookRepository;
import org.openapitools.model.BookStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookDAO> findAll() {
        return bookRepository.findAll();
    }

    public BookDAO createBook(String title, String description, String author) {
        return bookRepository.store(new BookDAO(title, author, description, BookStatus.AVAILABLE));
    }

    public Optional<BookDAO> findById(Long id) {
        return bookRepository.findById(id);
    }
}
