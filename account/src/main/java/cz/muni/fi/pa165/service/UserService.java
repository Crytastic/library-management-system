package cz.muni.fi.pa165.service;

import com.google.common.hash.Hashing;
import cz.muni.fi.pa165.data.model.UserDAO;
import cz.muni.fi.pa165.data.repository.UserRepository;
import org.openapitools.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
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

    public UserDAO createUser(String username, String password, String address, LocalDate birthDate, UserType userType) {
        String passwordHashed = Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString();
        return userRepository.saveUser(username, passwordHashed, address, birthDate, userType);
    }

    public Optional<UserDAO> findById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }


}
