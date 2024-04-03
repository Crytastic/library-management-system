package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.data.model.UserDAO;
import cz.muni.fi.pa165.service.UserService;
import org.openapitools.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFacade {

    private final UserService accountService;

    public UserFacade(@Autowired UserService accountService) {
        this.accountService = accountService;
    }

    public List<UserDTO> findAll() {
        List<UserDAO> users = accountService.findAll();
        return users.stream().map(dao -> new UserDTO()
                .address(dao.getAddress())
                .username(dao.getUsername())
                .birthDate(dao.getBirthDate()))
                .toList();
    }
}
