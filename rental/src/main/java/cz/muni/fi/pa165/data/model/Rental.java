package cz.muni.fi.pa165.data.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * This class encapsulates information about a rental.
 *
 * @author Maxmilián Šeffer
 */
public class Rental {
    private Long id;
    private String book;
    private String rentedBy;
    private OffsetDateTime borrowDate;
    private OffsetDateTime expectedReturnDate;
    private boolean returned;
    private OffsetDateTime returnDate;
    // Single currency for simplicity, e.g. EUR
    private BigDecimal lateReturnWeeklyFine;
    private boolean fineResolved;

    public Rental(String book, String rentedBy, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate, boolean returned, OffsetDateTime returnDate, BigDecimal lateReturnWeeklyFine, boolean fineResolved) {
        this.book = book;
        this.rentedBy = rentedBy;
        this.borrowDate = borrowDate;
        this.expectedReturnDate = expectedReturnDate;
        this.returned = returned;
        this.returnDate = returnDate;
        this.lateReturnWeeklyFine = lateReturnWeeklyFine;
        this.fineResolved = fineResolved;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getRentedBy() {
        return rentedBy;
    }

    public void setRentedBy(String rentedBy) {
        this.rentedBy = rentedBy;
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
        Rental rental = (Rental) o;
        return returned == rental.returned &&
                fineResolved == rental.fineResolved &&
                Objects.equals(id, rental.id) &&
                Objects.equals(book, rental.book) &&
                Objects.equals(rentedBy, rental.rentedBy) &&
                Objects.equals(borrowDate, rental.borrowDate) &&
                Objects.equals(expectedReturnDate, rental.expectedReturnDate) &&
                Objects.equals(returnDate, rental.returnDate) &&
                Objects.equals(lateReturnWeeklyFine, rental.lateReturnWeeklyFine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, book, rentedBy, borrowDate, expectedReturnDate, returned, returnDate, lateReturnWeeklyFine, fineResolved);
    }

    @Override
    public String toString() {
        return "Rental{" +
                "id=" + id +
                ", book='" + book +
                ", rentedBy='" + rentedBy +
                ", borrowDate=" + borrowDate +
                ", expectedReturnDate=" + expectedReturnDate +
                ", returned=" + returned +
                ", returnDate=" + returnDate +
                ", lateReturnWeeklyFine=" + lateReturnWeeklyFine +
                ", fineResolved=" + fineResolved +
                '}';
    }
}
