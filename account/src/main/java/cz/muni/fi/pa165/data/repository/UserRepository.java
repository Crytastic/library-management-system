package cz.muni.fi.pa165.data.repository;

import cz.muni.fi.pa165.data.model.UserDAO;
import org.openapitools.model.UserType;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    private Map<Long, UserDAO> users = new HashMap<>();

    private static Long index = 1L;

    public List<UserDAO> findAll() {
        return users.values()
                .stream()
                .toList();
    }

    public UserDAO saveUser(String username, String passwordHash, String address, LocalDate birthDate, UserType userType) {
        UserDAO newUser = new UserDAO(username, passwordHash, userType, address, birthDate);
        users.put(index, newUser);
        index++;
        return newUser;
    }
}
