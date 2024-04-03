package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.RentalFacade;
import org.openapitools.api.RentalApi;
import org.openapitools.model.Rental;
import org.openapitools.model.RentalTestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RentalController implements RentalApi {

    private final RentalFacade rentalFacade;

    @Autowired
    public RentalController(RentalFacade rentalFacade) {
        this.rentalFacade = rentalFacade;
    }


    @Override
    public ResponseEntity<RentalTestResponse> test() {
        return new ResponseEntity<>(new RentalTestResponse().message("Service running"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Rental>> getRentals() {
        return new ResponseEntity<>(rentalFacade.findAll(), HttpStatus.OK);
    }
}
