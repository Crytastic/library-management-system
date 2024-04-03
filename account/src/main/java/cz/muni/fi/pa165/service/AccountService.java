package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(@Autowired AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
