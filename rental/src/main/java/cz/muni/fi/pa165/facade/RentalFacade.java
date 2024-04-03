package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.dao.RentalDAO;
import cz.muni.fi.pa165.service.RentalService;
import org.openapitools.model.Rental;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    private Rental convertToDTO(RentalDAO rentalDAO) {
        return new Rental()
                .book(rentalDAO.getBook())
                .rentedBy(rentalDAO.getRentedBy())
                .borrowDate(rentalDAO.getBorrowDate());
    }
}