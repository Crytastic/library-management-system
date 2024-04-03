package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.AccountFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountRestController {

    private final AccountFacade accountFacade;

    public AccountRestController(@Autowired AccountFacade accountFacade) {
        this.accountFacade = accountFacade;
    }
}
