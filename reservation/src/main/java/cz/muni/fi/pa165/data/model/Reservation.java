package cz.muni.fi.pa165.data.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * This class encapsulates information about a reservation of a book.
 *
 * @author Martin Suchánek
 */
@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column()
    private Long id;
    @Column
    private String book;
    @Column
    private String reservedBy;
    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime reservedFrom;
    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime reservedTo;

    public Reservation(String book, String reservedBy, OffsetDateTime reservedFrom, OffsetDateTime reservedTo) {
        this.book = book;
        this.reservedBy = reservedBy;
        this.reservedFrom = reservedFrom;
        this.reservedTo = reservedTo;
    }

    public Reservation() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getReservedBy() {
        return reservedBy;
    }

    public void setReservedBy(String reservedBy) {
        this.reservedBy = reservedBy;
    }

    public OffsetDateTime getReservedFrom() {
        return reservedFrom;
    }

    public void setReservedFrom(OffsetDateTime reservedFrom) {
        this.reservedFrom = reservedFrom;
    }

    public OffsetDateTime getReservedTo() {
        return reservedTo;
    }

    public void setReservedTo(OffsetDateTime reservedTo) {
        this.reservedTo = reservedTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(book, that.book) &&
                Objects.equals(reservedBy, that.reservedBy) &&
                Objects.equals(reservedFrom, that.reservedFrom) &&
                Objects.equals(reservedTo, that.reservedTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, reservedBy, reservedFrom, reservedTo);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", book='" + book +
                ", reservedBy='" + reservedBy +
                ", reservedFrom=" + reservedFrom +
                ", reservedTo=" + reservedTo +
                '}';
    }
}
