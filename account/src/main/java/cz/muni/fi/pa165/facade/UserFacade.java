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

/**
 * Facade layer for managing user account.
 * Provides methods for interacting with user account properties.
 *
 * @author Sophia Zápotočná
 */
@Service
public class UserFacade {

    private final UserService userService;

    public UserFacade(@Autowired UserService userService) {
        this.userService = userService;
    }

    public List<UserDTO> findAll(UserType userType) {
        List<UserDAO> users = userService.findAll(userType);
        return users.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<UserDTO> findAllAdults() {
        List<UserDAO> users = userService.findAllAdults();
        return users.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public UserDTO createUser(String username, String password, String address, LocalDate birthDate, UserType userType) {
        UserDAO user = userService.createUser(username, password, address, birthDate, userType);
        return convertToDTO(user);
    }

    public Optional<UserDTO> findById(Long id) {
        return userService.findById(id).map(this::convertToDTO);
    }

    public void deleteById(Long id) {
        userService.deleteById(id);
    }

    public Optional<UserDTO> updateUser(Long id, String username, String password, String address, LocalDate birthdate, UserType userType) {
        return userService.updateUser(id, username, password, address, birthdate, userType)
                .map(this::convertToDTO);
    }

    private UserDTO convertToDTO(UserDAO userDAO) {
        return new UserDTO()
                .username(userDAO.getUsername())
                .address(userDAO.getAddress())
                .birthDate(userDAO.getBirthDate())
                .userType(userDAO.getUserType());
    }
}
