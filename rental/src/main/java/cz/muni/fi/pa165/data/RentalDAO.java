package cz.muni.fi.pa165.data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * This class encapsulates information about a rental.
 *
 * @author Maxmilián Šeffer
 */
public class RentalDAO {
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

    public RentalDAO(String book, String rentedBy, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate, boolean returned, OffsetDateTime returnDate, BigDecimal lateReturnWeeklyFine, boolean fineResolved) {
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
}
