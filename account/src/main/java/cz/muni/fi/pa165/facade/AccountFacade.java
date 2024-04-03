package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountFacade {

    private final AccountService accountService;

    public AccountFacade(@Autowired AccountService accountService) {
        this.accountService = accountService;
    }
}
