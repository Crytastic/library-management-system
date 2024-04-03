package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.UserFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    private final UserFacade accountFacade;

    public UserRestController(@Autowired UserFacade accountFacade) {
        this.accountFacade = accountFacade;
    }
}
