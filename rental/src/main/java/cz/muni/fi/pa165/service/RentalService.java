package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.dao.RentalDAO;
import cz.muni.fi.pa165.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public RentalDAO createRental(String book, String rentedBy, Date borrowDate) {
        return rentalRepository.store(new RentalDAO(book, rentedBy, borrowDate));
    }

    public Optional<RentalDAO> findById(Long id) {
        return rentalRepository.findById(id);
    }

    public void deleteById(Long id) {
        rentalRepository.deleteById(id);
    }

    public Optional<RentalDAO> updateById(Long id, String book, String rentedBy, Date borrowDate) {
        Optional<RentalDAO> optionalRental = rentalRepository.findById(id);

        if (optionalRental.isEmpty()) {
            return Optional.empty();
        }

        RentalDAO rentalDAO = optionalRental.get();

        if (book != null) rentalDAO.setBook(book);
        if (rentedBy != null) rentalDAO.setRentedBy(rentedBy);
        if (borrowDate != null) rentalDAO.setBorrowDate(borrowDate);

        return rentalRepository.updateById(rentalDAO.getId(), rentalDAO);
    }
}
