package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.UserDAO;
import cz.muni.fi.pa165.data.repository.UserRepository;
import cz.muni.fi.pa165.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findById_UserFound_ReturnsUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(TestDataFactory.userDAO));

        Optional<UserDAO> userDAO = userService.findById(1L);

        assertThat(userDAO).isPresent();
        assertThat(userDAO.get()).isEqualTo(TestDataFactory.userDAO);
    }

    @Test
    void findById_UserNotFound_ReturnsEmptyOptional() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserDAO> userDAO = userService.findById(1L);

        assertThat(userDAO).isEmpty();
    }
}