package cz.muni.fi.pa165.dao;

import org.openapitools.model.BookStatus;

public class BookDAO {
    Long id;

    String title;

    String author;

    String description;

    BookStatus status;

    public BookDAO(String title, String author, String description, BookStatus status) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}
