package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.dao.BookDAO;
import cz.muni.fi.pa165.repository.BookRepository;
import cz.muni.fi.pa165.repository.JpaBookRepository;
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

    private final JpaBookRepository jpaBookRepository;

    private final RentalServiceStub rentalServiceStub;

    @Autowired
    public BookService(BookRepository bookRepository, RentalServiceStub rentalServiceStub, JpaBookRepository jpaBookRepository) {
        this.bookRepository = bookRepository;
        this.rentalServiceStub = rentalServiceStub;
        this.jpaBookRepository = jpaBookRepository;
    }

    public List<BookDAO> findByFilter(String title, String author, String description, BookStatus status) {
        return bookRepository.findByFilter(title, author, description, status);
    }

    public BookDAO createBook(String title, String author, String description) {
        return jpaBookRepository.save(new BookDAO(title, author, description, BookStatus.AVAILABLE));
    }

    public Optional<BookDAO> findById(Long id) {
        return bookRepository.findById(id);
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    public Optional<BookDAO> updateById(Long id, String title, String author, String description, BookStatus status) {
        Optional<BookDAO> optionalBook = bookRepository.findById(id);

        if (optionalBook.isEmpty()) {
            return Optional.empty();
        }

        BookDAO bookDao = optionalBook.get();

        if (title != null) bookDao.setTitle(title);
        if (author != null) bookDao.setAuthor(author);
        if (description != null) bookDao.setDescription(description);
        if (status != null) bookDao.setStatus(status);

        return bookRepository.updateById(id, bookDao);
    }

    public Optional<List<String>> findBookRentals(Long id) {
        if (bookRepository.findById(id).isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(rentalServiceStub.apiCallToRentalServiceToFindBookRentals(id));
        }
    }
}
