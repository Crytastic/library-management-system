package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.mappers.UserMapper;
import cz.muni.fi.pa165.service.UserService;
import org.openapitools.model.UserDTO;
import org.openapitools.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Facade layer for managing user.
 * Provides methods for interacting with user properties.
 *
 * @author Sophia Zápotočná
 */
@Service
public class UserFacade {
    private final UserService userService;

    private final UserMapper userMapper;

    public UserFacade(@Autowired UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    public List<UserDTO> findAll(UserType userType) {
        return userMapper.mapToList(userService.findAll(userType));
    }

    public List<UserDTO> findAllAdults() {
        return userMapper.mapToList(userService.findAllAdults());
    }

    public UserDTO createUser(String username, String password, String address, LocalDate birthDate, UserType userType) {
        return userMapper.mapToDto(userService.createUser(username, password, address, birthDate, userType));
    }

    public UserDTO findById(Long id) {
        return userMapper.mapToDto(userService.findById(id));
    }

    public void deleteById(Long id) {
        userService.deleteById(id);
    }

    public UserDTO updateUser(Long id, String username, String password, String address, LocalDate birthdate, UserType userType) {
        return userMapper.mapToDto(userService.updateUser(id, username, password, address, birthdate, userType));
    }

    public void deleteAll() {
        userService.deleteAll();
    }
}
