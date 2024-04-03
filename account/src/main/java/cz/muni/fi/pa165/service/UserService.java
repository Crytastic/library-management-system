package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.UserDAO;
import cz.muni.fi.pa165.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository accountRepository;

    public UserService(@Autowired UserRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<UserDAO> findAll() {
        return accountRepository.findAll();
    }
}
