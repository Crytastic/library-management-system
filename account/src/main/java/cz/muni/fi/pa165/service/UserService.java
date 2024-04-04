package cz.muni.fi.pa165.service;

import com.google.common.hash.Hashing;
import cz.muni.fi.pa165.data.model.UserDAO;
import cz.muni.fi.pa165.data.repository.UserRepository;
import cz.muni.fi.pa165.exception.UnauthorisedException;
import io.micrometer.observation.ObservationFilter;
import org.openapitools.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDAO> findAll(UserType userType) {
        return userRepository.findAll(userType);
    }

    public List<UserDAO> findAllAdults() {
        return userRepository.findAllAdults();
    }

    public UserDAO createUser(String username, String password, String address, LocalDate birthDate, UserType userType) {
        String passwordHashed = createPasswordHash(password);
        return userRepository.saveUser(username, passwordHashed, address, birthDate, userType);
    }

    private String createPasswordHash(String password) {
        return Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
    }

    public Optional<UserDAO> findById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }


    /**
     * Checks that username exists in database and the password is correct.
     * The LIBRARIANS can change any of parameters of any USER.
     * The MEMBERS can change only themselves, they can not change a type of user.
     */
    public Optional<UserDAO> updateUser(Long id, String username, String password, String address, LocalDate birthdate, UserType userType) throws UnauthorisedException {
        UserDAO userByUsername = userRepository.findUserByUsername(username);
        if (userByUsername == null || !userByUsername.getPasswordHash().equals(createPasswordHash(password))) {
            throw new UnauthorisedException();
        }
        if (userByUsername.getUserType().equals(UserType.MEMBER)) {
            if (!Objects.equals(userByUsername.getId(), id) || userType != null) {
                throw new UnauthorisedException();
            }
        }
        Optional<UserDAO> optionalUpdatedUser = userRepository.findById(id);
        if (optionalUpdatedUser.isEmpty()) {
            return Optional.empty();
        }
        UserDAO updatedUser = optionalUpdatedUser.get();
        if (userType != null) updatedUser.setUserType(userType);
        if (address != null) updatedUser.setAddress(address);
        if (birthdate != null) updatedUser.setBirthDate(birthdate);

        return Optional.ofNullable(userRepository.updateUser(updatedUser.getId(), updatedUser));
    }
}
