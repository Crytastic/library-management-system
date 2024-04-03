package cz.muni.fi.pa165.rest;

import org.openapitools.api.RentalApi;
import org.openapitools.model.RentalTestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class RentalController implements RentalApi {

    @Override
    public ResponseEntity<RentalTestResponse> test() {
        return new ResponseEntity<>(new RentalTestResponse().message("Service running"), HttpStatus.OK);
    }
}
