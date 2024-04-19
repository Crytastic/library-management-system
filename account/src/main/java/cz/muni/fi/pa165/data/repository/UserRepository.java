package cz.muni.fi.pa165.data.repository;

import cz.muni.fi.pa165.data.model.User;
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

    private Map<Long, User> users = new HashMap<>();
    private static Long index = 1L;
    private static final int ADULT_AGE = 18;

    public List<User> findAll(UserType userType) {
        return users.values()
                .stream()
                .filter(user -> userType == null || user.getUserType().equals(userType))
                .toList();
    }

    public List<User> findAllAdults() {
        return users.values()
                .stream()
                .filter(user -> getAge(user.getBirthDate()) >= ADULT_AGE)
                .toList();
    }

    public User saveUser(String username, String passwordHash, String address, LocalDate birthDate, UserType userType) {
        User newUser = new User(username, passwordHash, userType, address, birthDate);
        users.put(index, newUser);
        index++;
        return newUser;
    }

    public Optional<User> findById(Long id) {
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

    public User findUserByUsername(String username) {
        List<User> optionalUser = users.values()
                .stream()
                .filter(user -> user.getUsername().equals(username))
                .toList();
        if (optionalUser.isEmpty()) {
            return null;
        }
        return optionalUser.getFirst();
    }

    public User updateUser(Long id, User userToUpdate) {
        users.put(id, userToUpdate);
        return users.get(id);
    }
}
