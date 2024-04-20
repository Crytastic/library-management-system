package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.Book;
import cz.muni.fi.pa165.data.repository.JpaBookRepository;
import cz.muni.fi.pa165.stubs.RentalServiceStub;
import org.openapitools.model.BookStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final JpaBookRepository jpaBookRepository;

    private final RentalServiceStub rentalServiceStub;

    @Autowired
    public BookService(RentalServiceStub rentalServiceStub, JpaBookRepository jpaBookRepository) {
        this.rentalServiceStub = rentalServiceStub;
        this.jpaBookRepository = jpaBookRepository;
    }

    @Transactional
    public List<Book> findByFilter(String title, String author, String description, BookStatus status) {
        return jpaBookRepository.findByFilter(title, author, description, status);
    }

    @Transactional
    public Book createBook(String title, String author, String description) {
        return jpaBookRepository.save(new Book(title, author, description, BookStatus.AVAILABLE));
    }

    @Transactional
    public Optional<Book> findById(Long id) {
        return jpaBookRepository.findById(id);
    }

    @Transactional
    public void deleteById(Long id) { jpaBookRepository.deleteById(id);
    }

    @Transactional
    public int updateById(Long id, String title, String author, String description, BookStatus status) {
        return jpaBookRepository.updateById(id, title, author, description, status);
    }

    @Transactional
    public Optional<List<String>> findBookRentals(Long id) {
        if (jpaBookRepository.findById(id).isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(rentalServiceStub.apiCallToRentalServiceToFindBookRentals(id));
        }
    }
}
