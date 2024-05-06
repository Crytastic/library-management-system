package cz.muni.fi.pa165.config;

import cz.muni.fi.pa165.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InsertInitialDataService {
    private final UserRepository userRepository;

    @Autowired
    public InsertInitialDataService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
