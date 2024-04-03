package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserFacade {

    private final UserService accountService;

    public UserFacade(@Autowired UserService accountService) {
        this.accountService = accountService;
    }
}
