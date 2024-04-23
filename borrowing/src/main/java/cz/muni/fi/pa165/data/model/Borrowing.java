package cz.muni.fi.pa165.data.model;

import cz.muni.fi.pa165.util.ObjectConverter;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * This class encapsulates information about a borrowing.
 *
 * @author Maxmilián Šeffer
 */
@Entity
public class Borrowing {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "borrowing_id")
    private Long id;
    @Column
    private Long bookId;
    @Column
    private Long borrowerId;
    @Column
    private OffsetDateTime borrowDate;
    @Column
    private OffsetDateTime expectedReturnDate;
    @Column
    private boolean returned;
    @Column
    private OffsetDateTime returnDate;
    // Single currency for simplicity, e.g. EUR
    @Column
    private BigDecimal lateReturnWeeklyFine;
    @Column
    private boolean fineResolved;

    public Borrowing(Long bookId, Long borrowerId, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate, boolean returned, OffsetDateTime returnDate, BigDecimal lateReturnWeeklyFine, boolean fineResolved) {
        this.bookId = bookId;
        this.borrowerId = borrowerId;
        this.borrowDate = borrowDate;
        this.expectedReturnDate = expectedReturnDate;
        this.returned = returned;
        this.returnDate = returnDate;
        this.lateReturnWeeklyFine = lateReturnWeeklyFine;
        this.fineResolved = fineResolved;
    }

    public Borrowing() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBook(Long bookId) {
        this.bookId = bookId;
    }

    public Long getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowedBy(Long borrowerId) {
        this.borrowerId = borrowerId;
    }

    public OffsetDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(OffsetDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public OffsetDateTime getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(OffsetDateTime expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

    public OffsetDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(OffsetDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public BigDecimal getLateReturnWeeklyFine() {
        return lateReturnWeeklyFine;
    }

    public void setLateReturnWeeklyFine(BigDecimal lateReturnWeeklyFine) {
        this.lateReturnWeeklyFine = lateReturnWeeklyFine;
    }

    public boolean isFineResolved() {
        return fineResolved;
    }

    public void setFineResolved(boolean fineResolved) {
        this.fineResolved = fineResolved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Borrowing borrowing = (Borrowing) o;
        return returned == borrowing.returned &&
                fineResolved == borrowing.fineResolved &&
                Objects.equals(id, borrowing.id) &&
                Objects.equals(bookId, borrowing.bookId) &&
                Objects.equals(borrowerId, borrowing.borrowerId) &&
                Objects.equals(borrowDate, borrowing.borrowDate) &&
                Objects.equals(expectedReturnDate, borrowing.expectedReturnDate) &&
                Objects.equals(returnDate, borrowing.returnDate) &&
                Objects.equals(lateReturnWeeklyFine, borrowing.lateReturnWeeklyFine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookId, borrowerId, borrowDate, expectedReturnDate, returned, returnDate, lateReturnWeeklyFine, fineResolved);
    }

    @Override
    public String toString() {
        return ObjectConverter.convertObjectToJsonWithClassnamePrefix(this);
    }
}
