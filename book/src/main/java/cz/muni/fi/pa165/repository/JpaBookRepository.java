package cz.muni.fi.pa165.repository;

import cz.muni.fi.pa165.dao.BookDAO;
import org.openapitools.model.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Book;
import java.util.List;

@Repository
public interface JpaBookRepository extends JpaRepository<BookDAO, Long> {

    @Modifying
    @Query("UPDATE BookDAO b SET " +
            "b.author = coalesce(:author, b.author), " +
            "b.title = coalesce(:title, b.title)," +
            "b.description = coalesce(:description, b.description)," +
            "b.status = coalesce(:status, b.status) WHERE b.id = :id")
    int updateById(Long id, String title, String author, String description, BookStatus status);

    @Query("SELECT b FROM BookDAO b WHERE " +
            "b.author = coalesce(:author, b.author) AND " +
            "b.title = coalesce(:title, b.title) AND " +
            "b.description = coalesce(:description, b.description) AND " +
            "b.status = coalesce(:status, b.status)")
    List<BookDAO> findByFilter(String title,String author,String description,BookStatus status);

}
