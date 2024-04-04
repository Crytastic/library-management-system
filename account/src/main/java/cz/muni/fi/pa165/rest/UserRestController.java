package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.UserFacade;
import org.openapitools.api.UserApi;
import org.openapitools.model.UserDTO;
import org.openapitools.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
public class UserRestController implements UserApi {

    private final UserFacade userFacade;

    public UserRestController(@Autowired UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Override
    public ResponseEntity<UserDTO> createUser(String username, String password, String address, LocalDate birthDate, UserType userType) {
        return new ResponseEntity<>(userFacade.createUser(username, password, address, birthDate, userType), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<UserDTO> getUser(Long id) {
        Optional<UserDTO> user = userFacade.findById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Override
    public ResponseEntity<List<UserDTO>> getUsers() {
        return new ResponseEntity<>(userFacade.findAll(), HttpStatus.OK);
    }


}
