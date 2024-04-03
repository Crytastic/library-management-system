package cz.muni.fi.pa165.dto;

import org.openapitools.model.Book;

public class BookDTO {
    Long id;

    String title;

    String author;

    String description;

    Book.StatusEnum status;

    public BookDTO(String title, String author, String description, Book.StatusEnum status) {
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

    public Book.StatusEnum getStatus() {
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

    public void setStatus(Book.StatusEnum status) {
        this.status = status;
    }
}
