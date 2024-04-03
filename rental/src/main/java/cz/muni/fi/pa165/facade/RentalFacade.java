package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.dao.RentalDAO;
import cz.muni.fi.pa165.service.RentalService;
import org.openapitools.model.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RentalFacade {

    private final RentalService rentalService;

    @Autowired
    public RentalFacade(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    public List<Rental> findAll() {
        return rentalService
                .findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Rental createRental(String book, String rentedBy, OffsetDateTime borrowDate) {
        RentalDAO rentalDAO = rentalService.createRental(book, rentedBy, borrowDate);
        return convertToDTO(rentalDAO);
    }

    public Optional<Rental> findById(Long id) {
        return rentalService.findById(id).map(this::convertToDTO);
    }

    public boolean deleteById(Long id) {
        return rentalService.deleteById(id);
    }

    public Optional<Rental> updateById(Long id, String book, String rentedBy, OffsetDateTime borrowDate) {
        return rentalService.updateById(id, book, rentedBy, borrowDate).map(this::convertToDTO);
    }

    private Rental convertToDTO(RentalDAO rentalDAO) {
        return new Rental()
                .book(rentalDAO.getBook())
                .rentedBy(rentalDAO.getRentedBy())
                .borrowDate(rentalDAO.getBorrowDate());
    }
}