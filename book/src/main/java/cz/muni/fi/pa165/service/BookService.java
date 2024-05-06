package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.Book;
import cz.muni.fi.pa165.data.repository.BookRepository;
import cz.muni.fi.pa165.exceptionhandling.exceptions.ResourceNotFoundException;
import cz.muni.fi.pa165.stubs.BorrowingServiceStub;
import org.openapitools.model.BookStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for managing books.
 * Provides methods to interact with books.
 *
 * @author Sophia Zápotočná
 */
@Service
public class BookService {
    private final BookRepository bookRepository;

    private final BorrowingServiceStub borrowingServiceStub;

    @Autowired
    public BookService(BorrowingServiceStub borrowingServiceStub, BookRepository bookRepository) {
        this.borrowingServiceStub = borrowingServiceStub;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public List<Book> findByFilter(String title, String author, String description, BookStatus status) {
        return bookRepository.findByFilter(title, author, description, status);
    }

    @Transactional
    public Book createBook(String title, String author, String description) {
        return bookRepository.save(new Book(title, author, description, BookStatus.AVAILABLE));
    }

    @Transactional
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Book with id: %d not found", id)));
    }

    @Transactional
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public int updateById(Long id, String title, String author, String description, BookStatus status) {
        int updatedCount = bookRepository.updateById(id, title, author, description, status);
        if (updatedCount > 0) {
            return updatedCount;
        } else {
            throw new ResourceNotFoundException(String.format("Book with id: %d not found", id));
        }
    }

    @Transactional
    public List<String> findBookBorrowings(Long id) {
        if (bookRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException(String.format("Book with id: %d not found", id));
        } else {
            return borrowingServiceStub.apiCallToBorrowingServiceToFindBookBorrowings(id);
        }
    }

    public void deleteAll() {
        bookRepository.deleteAll();
    }
}
