package cz.muni.fi.pa165.service;

import com.google.common.hash.Hashing;
import cz.muni.fi.pa165.data.model.User;
import cz.muni.fi.pa165.data.repository.JpaUserRepository;
import cz.muni.fi.pa165.data.repository.UserRepository;
import cz.muni.fi.pa165.exceptions.UnauthorisedException;
import cz.muni.fi.pa165.exceptions.UsernameAlreadyExistsException;
import org.openapitools.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service layer for managing user account.
 * Provides methods to interact with user account.
 *
 * @author Sophia Zápotočná
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    private final JpaUserRepository jpaUserRepository;

    public UserService(@Autowired UserRepository userRepository, @Autowired JpaUserRepository jpaUserRepository) {
        this.userRepository = userRepository;
        this.jpaUserRepository = jpaUserRepository;
    }

    public List<User> findAll(UserType userType) {
        return jpaUserRepository.findAllByUserType(userType);
    }

    public List<User> findAllAdults() {
        return userRepository.findAllAdults();
    }

    public User createUser(String username, String password, String address, LocalDate birthDate, UserType userType) {
        String passwordHashed = createPasswordHash(password);
        try {
            return jpaUserRepository.save(new User(username, passwordHashed, userType, address, birthDate));
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExistsException(e.getMessage());
        }
    }

    private String createPasswordHash(String password) {
        return Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }

    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id);
    }

    public void deleteById(Long id) {
        jpaUserRepository.deleteById(id);
    }


    /**
     * Checks that username exists in database and the password is correct.
     * The LIBRARIANS can change any of parameters of any USER.
     * The MEMBERS can change only themselves, they can not change a type of user.
     */
    public Optional<User> updateUser(Long id, String username, String password, String address, LocalDate birthdate, UserType userType) {
        User userByUsername = userRepository.findUserByUsername(username);
        if (userByUsername == null || !userByUsername.getPasswordHash().equals(createPasswordHash(password))) {
            throw new UnauthorisedException();
        }
        if (userByUsername.getUserType().equals(UserType.MEMBER)) {
            if (!Objects.equals(userByUsername.getId(), id) || userType != null) {
                throw new UnauthorisedException();
            }
        }
        Optional<User> optionalUpdatedUser = jpaUserRepository.findById(id);
        if (optionalUpdatedUser.isEmpty()) {
            return Optional.empty();
        }
        User updatedUser = optionalUpdatedUser.get();
        if (userType != null) updatedUser.setUserType(userType);
        if (address != null) updatedUser.setAddress(address);
        if (birthdate != null) updatedUser.setBirthDate(birthdate);

        return Optional.ofNullable(userRepository.updateUser(updatedUser.getId(), updatedUser));
    }
}
