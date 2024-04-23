package cz.muni.fi.pa165.data.model;

import cz.muni.fi.pa165.util.ObjectConverter;
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
    private Long bookId;
    @Column
    private Long reserveeId;
    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime reservedFrom;
    @Column
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime reservedTo;

    public Reservation(Long bookId, Long reserveeId, OffsetDateTime reservedFrom, OffsetDateTime reservedTo) {
        this.bookId = bookId;
        this.reserveeId = reserveeId;
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

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getReserveeId() {
        return reserveeId;
    }

    public void setReserveeId(Long reserveeId) {
        this.reserveeId = reserveeId;
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
                Objects.equals(bookId, that.bookId) &&
                Objects.equals(reserveeId, that.reserveeId) &&
                Objects.equals(reservedFrom, that.reservedFrom) &&
                Objects.equals(reservedTo, that.reservedTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookId, reserveeId, reservedFrom, reservedTo);
    }

    @Override
    public String toString() {
        return ObjectConverter.convertObjectToJsonWithClassnamePrefix(this);
    }
}
