package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.data.model.UserDAO;
import cz.muni.fi.pa165.service.UserService;
import org.openapitools.model.UserDTO;
import org.openapitools.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserFacade {

    private final UserService userService;

    public UserFacade(@Autowired UserService userService) {
        this.userService = userService;
    }

    public List<UserDTO> findAll() {
        List<UserDAO> users = userService.findAll();
        return users.stream().map(dao -> new UserDTO()
                .address(dao.getAddress())
                .username(dao.getUsername())
                .birthDate(dao.getBirthDate())
                .userType(dao.getUserType()))
                .toList();
    }

    public UserDTO createUser(String username, String password, String address, LocalDate birthDate, UserType userType) {
        UserDAO user = userService.createUser(username, password, address, birthDate, userType);
        return new UserDTO()
                .username(user.getUsername())
                .address(user.getAddress())
                .birthDate(user.getBirthDate())
                .userType(user.getUserType());
    }

    public Optional<UserDTO> findById(Long id) {
        return userService.findById(id).map(dao -> new UserDTO()
                .username(dao.getUsername())
                .address(dao.getAddress())
                .birthDate(dao.getBirthDate())
                .userType(dao.getUserType()));
    }
}
