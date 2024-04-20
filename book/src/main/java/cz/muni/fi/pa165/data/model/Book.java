package cz.muni.fi.pa165.data.model;

import org.openapitools.model.BookStatus;

import java.util.Objects;

/**
 * This class encapsulates information about a book.
 *
 * @author Martin Suchánek
 */
public class Book {
    Long id;
    String title;
    String author;
    String description;
    BookStatus status;

    public Book(String title, String author, String description, BookStatus status) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author) &&
                Objects.equals(description, book.description) &&
                status == book.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, description, status);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title +
                ", author='" + author +
                ", description='" + description +
                ", status=" + status +
                '}';
    }
}
