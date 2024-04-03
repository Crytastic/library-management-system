package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.UserFacade;
import org.openapitools.api.UserApi;
import org.openapitools.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserRestController implements UserApi {

    private final UserFacade accountFacade;

    public UserRestController(@Autowired UserFacade accountFacade) {
        this.accountFacade = accountFacade;
    }

    @Override
    public ResponseEntity<List<UserDTO>> getUsers() {
        return new ResponseEntity<>(accountFacade.findAll(), HttpStatus.OK);
    }
}
