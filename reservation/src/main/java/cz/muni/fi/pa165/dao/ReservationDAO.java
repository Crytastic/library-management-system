package cz.muni.fi.pa165.dao;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

/**
 * This class encapsulates information about a reservation of a book.
 *
 * @author Martin Suchánek
 */
public class ReservationDAO {
    private Long id;
    private String book;
    private String reservedBy;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime reservedFrom;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private OffsetDateTime reservedTo;

    public ReservationDAO(String book, String reservedBy, OffsetDateTime reservedFrom, OffsetDateTime reservedTo) {
        this.book = book;
        this.reservedBy = reservedBy;
        this.reservedFrom = reservedFrom;
        this.reservedTo = reservedTo;
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
}
