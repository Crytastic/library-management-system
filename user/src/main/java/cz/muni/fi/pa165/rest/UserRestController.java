package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.UserFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.openapitools.api.UserApi;
import org.openapitools.model.UserDTO;
import org.openapitools.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller layer for managing users.
 * Handles HTTP requests related to user management.
 *
 * @author Sophia Zápotočná
 */

@RestController
public class UserRestController implements UserApi {
    private final UserFacade userFacade;

    public UserRestController(@Autowired UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Override
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserDTO> createUser(String username, String password, String address, LocalDate birthDate, UserType userType) {
        return new ResponseEntity<>(userFacade.createUser(username, password, address, birthDate, userType), HttpStatus.CREATED);
    }

    @Override
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteUser(Long id) {
        userFacade.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @Override
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserDTO> getUser(Long id) {
        return new ResponseEntity<>(userFacade.findById(id), HttpStatus.OK);
    }

    @Override
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<UserDTO>> getUsers(UserType userType) {
        return new ResponseEntity<>(userFacade.findAll(userType), HttpStatus.OK);
    }

    @Override
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<UserDTO>> getAdultUsers() {
        return new ResponseEntity<>(userFacade.findAllAdults(), HttpStatus.OK);
    }

    @Override
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<UserDTO> updateUser(Long id, String username, String password, String address, LocalDate birthdate, UserType userType) {
        return new ResponseEntity<>(userFacade.updateUser(id, username, password, address, birthdate, userType), HttpStatus.OK);
    }

    @Override
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteUsers() {
        userFacade.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
