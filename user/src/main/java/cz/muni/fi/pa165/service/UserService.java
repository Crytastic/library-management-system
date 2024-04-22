package cz.muni.fi.pa165.service;

import com.google.common.hash.Hashing;
import cz.muni.fi.pa165.data.model.User;
import cz.muni.fi.pa165.data.repository.UserRepository;
import cz.muni.fi.pa165.exceptions.UnauthorisedException;
import cz.muni.fi.pa165.exceptions.UsernameAlreadyExistsException;
import org.openapitools.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service layer for managing user.
 * Provides methods to interact with user.
 *
 * @author Sophia Zápotočná
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public List<User> findAll(UserType userType) {
        return userRepository.findAllByUserType(userType);
    }

    @Transactional
    public List<User> findAllAdults() {
        return userRepository.findAllByAge(getDateForAdultAge());
    }

    @Transactional
    public User createUser(String username, String password, String address, LocalDate birthDate, UserType userType) {
        String passwordHashed = createPasswordHash(password);
        try {
            return userRepository.save(new User(username, passwordHashed, userType, address, birthDate));
        } catch (DataIntegrityViolationException e) {
            throw new UsernameAlreadyExistsException(e.getMessage());
        }
    }

    private String createPasswordHash(String password) {
        return Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }

    @Transactional
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }


    /**
     * Checks that username exists in database and the password is correct.
     * The LIBRARIANS can change any of parameters of any USER.
     * The MEMBERS can change only themselves, they can not change a type of user.
     */
    @Transactional
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
        Optional<User> optionalUpdatedUser = userRepository.findById(id);
        if (optionalUpdatedUser.isEmpty()) {
            return Optional.empty();
        }
        User updatedUser = optionalUpdatedUser.get();
        if (userType != null) updatedUser.setUserType(userType);
        if (address != null) updatedUser.setAddress(address);
        if (birthdate != null) updatedUser.setBirthDate(birthdate);

        return Optional.of(userRepository.save(updatedUser));
    }

    private LocalDate getDateForAdultAge() {
        return LocalDate.now().minusYears(18);
    }
}