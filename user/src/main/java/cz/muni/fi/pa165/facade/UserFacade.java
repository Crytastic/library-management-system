package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.data.model.User;
import cz.muni.fi.pa165.service.UserService;
import org.openapitools.model.UserDTO;
import org.openapitools.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Facade layer for managing user.
 * Provides methods for interacting with user properties.
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
        List<User> users = userService.findAll(userType);
        return users.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<UserDTO> findAllAdults() {
        List<User> users = userService.findAllAdults();
        return users.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public UserDTO createUser(String username, String password, String address, LocalDate birthDate, UserType userType) {
        User user = userService.createUser(username, password, address, birthDate, userType);
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

    // Will be replaced by mapper in the future
    private UserDTO convertToDTO(User user) {
        return new UserDTO()
                .id(user.getId())
                .username(user.getUsername())
                .address(user.getAddress())
                .birthDate(user.getBirthDate())
                .userType(user.getUserType());
    }
}
