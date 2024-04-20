package cz.muni.fi.pa165.data.repository;

import cz.muni.fi.pa165.data.model.Book;
import org.openapitools.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Modifying
    @Query("UPDATE Book b SET " +
            "b.author = coalesce(:author, b.author), " +
            "b.title = coalesce(:title, b.title)," +
            "b.description = coalesce(:description, b.description)," +
            "b.status = coalesce(:status, b.status) WHERE b.id = :id")
    int updateById(Long id, String title, String author, String description, BookStatus status);

    @Query("SELECT b FROM Book b WHERE " +
            "b.author = coalesce(:author, b.author) AND " +
            "b.title = coalesce(:title, b.title) AND " +
            "b.description = coalesce(:description, b.description) AND " +
            "b.status = coalesce(:status, b.status)")
    List<Book> findByFilter(String title, String author, String description, BookStatus status);

}
