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

        return bookRepository.updateById(bookDao.getId(), bookDao);

    }
}
