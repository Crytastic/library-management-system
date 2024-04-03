package cz.muni.fi.pa165.dao;

import java.util.Date;

public class RentalDAO {
    private Long id;
    private String book;
    private String rentedBy;
    private Date borrowDate;

    public RentalDAO(String book, String rentedBy, Date borrowDate) {
        this.book = book;
        this.rentedBy = rentedBy;
        this.borrowDate = borrowDate;
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

    public Date getBorrowDate() {
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

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }
}
