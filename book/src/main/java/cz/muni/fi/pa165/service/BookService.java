package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.Book;
import cz.muni.fi.pa165.repository.BookRepository;
import cz.muni.fi.pa165.stubs.RentalServiceStub;
import org.openapitools.model.BookStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing books.
 * Provides methods to interact with books.
 *
 * @author Sophia Zápotočná
 */
@Service
public class BookService {

    private final BookRepository bookRepository;

    private final RentalServiceStub rentalServiceStub;

    @Autowired
    public BookService(BookRepository bookRepository, RentalServiceStub rentalServiceStub) {
        this.bookRepository = bookRepository;
        this.rentalServiceStub = rentalServiceStub;
    }

    public List<Book> findByFilter(String title, String author, String description, BookStatus status) {
        return bookRepository.findByFilter(title, author, description, status);
    }

    public Book createBook(String title, String author, String description) {
        return bookRepository.store(new Book(title, author, description, BookStatus.AVAILABLE));
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    public Optional<Book> updateById(Long id, String title, String author, String description, BookStatus status) {
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isEmpty()) {
            return Optional.empty();
        }

        Book book = optionalBook.get();

        if (title != null) book.setTitle(title);
        if (author != null) book.setAuthor(author);
        if (description != null) book.setDescription(description);
        if (status != null) book.setStatus(status);

        return bookRepository.updateById(id, book);
    }

    public Optional<List<String>> findBookRentals(Long id) {
        if (bookRepository.findById(id).isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(rentalServiceStub.apiCallToRentalServiceToFindBookRentals(id));
        }
    }
}
