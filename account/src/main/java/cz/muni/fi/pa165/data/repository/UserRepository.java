package cz.muni.fi.pa165.data.repository;

import cz.muni.fi.pa165.data.model.UserDAO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    private Map<Long, UserDAO> users = new HashMap<>();

    public List<UserDAO> findAll() {
        return users.values()
                .stream()
                .toList();
    }
}
