package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.dao.RentalDAO;
import cz.muni.fi.pa165.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;

    @Autowired
    public RentalService(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    public List<RentalDAO> findAll() {
        return rentalRepository.findAll();
    }

    public RentalDAO createRental(String book, String rentedBy, OffsetDateTime borrowDate) {
        OffsetDateTime expectedReturnDate = borrowDate.plusMonths(3);
        return createRental(book, rentedBy, borrowDate, expectedReturnDate);
    }

    public RentalDAO createRental(String book, String rentedBy, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate) {
        RentalDAO rentalDAO = new RentalDAO(book, rentedBy, borrowDate, expectedReturnDate, false, null);
        return rentalRepository.store(rentalDAO);
    }


    public Optional<RentalDAO> findById(Long id) {
        return rentalRepository.findById(id);
    }

    public boolean deleteById(Long id) {
        return rentalRepository.deleteById(id);
    }

    public Optional<RentalDAO> updateById(Long id, String book, String rentedBy, OffsetDateTime borrowDate, OffsetDateTime expectedReturnDate, boolean returned, OffsetDateTime returnDate) {
        Optional<RentalDAO> optionalRental = rentalRepository.findById(id);

        if (optionalRental.isEmpty()) {
            return Optional.empty();
        }

        RentalDAO rentalDAO = optionalRental.get();

        rentalDAO.setBook(book != null ? book : rentalDAO.getBook());
        rentalDAO.setRentedBy(rentedBy != null ? rentedBy : rentalDAO.getRentedBy());
        rentalDAO.setBorrowDate(borrowDate != null ? borrowDate : rentalDAO.getBorrowDate());
        rentalDAO.setExpectedReturnDate(expectedReturnDate != null ? expectedReturnDate : rentalDAO.getExpectedReturnDate());
        if (returned) {
            rentalDAO.setReturned(true);
            rentalDAO.setReturnDate(returnDate != null ? returnDate : rentalDAO.getReturnDate());
        }

        return rentalRepository.updateById(rentalDAO.getId(), rentalDAO);
    }
}
