package cz.muni.fi.pa165.dao;

import java.time.OffsetDateTime;

public class RentalDAO {
    private Long id;
    private String book;
    private String rentedBy;
    private OffsetDateTime borrowDate;
    private OffsetDateTime expectedReturnDate;
    private boolean returned;
    private OffsetDateTime returnDate;

    public RentalDAO(String book, String rentedBy, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate, boolean returned, OffsetDateTime returnDate) {
        this.book = book;
        this.rentedBy = rentedBy;
        this.borrowDate = borrowDate;
        this.expectedReturnDate = expectedReturnDate;
        this.returned = returned;
        this.returnDate = returnDate;
    }

    public Long getId() {
        return id;
    }

    public String getBook() {
        return book;
    }

    public String getRentedBy() {
        return rentedBy;
    }

    public OffsetDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public void setRentedBy(String rentedBy) {
        this.rentedBy = rentedBy;
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
}
