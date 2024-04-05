package cz.muni.fi.pa165.data.repository;

import cz.muni.fi.pa165.data.model.UserDAO;
import org.openapitools.model.UserType;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Repository layer for managing user account.
 * Provides methods for storing, retrieving and updating, user account.
 *
 * @author Sophia Zápotočná
 */

@Repository
public class UserRepository {

    private Map<Long, UserDAO> users = new HashMap<>();

    private static Long index = 1L;

    private static final int ADULT_AGE = 18;

    public List<UserDAO> findAll(UserType userType) {
        return users.values()
                .stream()
                .filter(user -> userType == null || user.getUserType().equals(userType))
                .toList();
    }

    public List<UserDAO> findAllAdults() {
        return users.values()
                .stream()
                .filter(user -> getAge(user.getBirthDate()) >= ADULT_AGE)
                .toList();
    }

    public UserDAO saveUser(String username, String passwordHash, String address, LocalDate birthDate, UserType userType) {
        UserDAO newUser = new UserDAO(index, username, passwordHash, userType, address, birthDate);
        users.put(index, newUser);
        index++;
        return newUser;
    }

    public Optional<UserDAO> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public void deleteById(Long id) {
        users.remove(id);
    }

    private int getAge(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        return period.getYears();
    }

    public UserDAO findUserByUsername(String username) {
        List<UserDAO> optionalUser = users.values()
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .toList();
        if (optionalUser.isEmpty()) {
            return null;
        }
        return optionalUser.getFirst();
    }

    public UserDAO updateUser(Long id, UserDAO userToUpdate) {
        users.put(id, userToUpdate);
        return users.get(id);
    }
}
