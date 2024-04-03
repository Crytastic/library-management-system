package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RentalFacade {

    private final RentalService rentalService;

    @Autowired
    public RentalFacade(RentalService rentalService) {
        this.rentalService = rentalService;
    }
}