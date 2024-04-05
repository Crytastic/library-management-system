package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.UserDAO;
import cz.muni.fi.pa165.data.repository.UserRepository;
import cz.muni.fi.pa165.exceptions.UsernameAlreadyExistsException;
import cz.muni.fi.pa165.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.UserType;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findById_userFound_returnsUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(TestDataFactory.firstMemberDAO));

        Optional<UserDAO> userDAO = userService.findById(1L);

        assertThat(userDAO).isPresent();
        assertThat(userDAO.get()).isEqualTo(TestDataFactory.firstMemberDAO);
    }

    @Test
    void findById_userNotFound_returnsEmptyOptional() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserDAO> userDAO = userService.findById(1L);

        assertThat(userDAO).isEmpty();
    }

    @Test
    void createUser_usernameNotExists_returnsNewUser() {
        String username = "programmer123";
        String passwordHash = "passwordHash";
        UserType userType = UserType.MEMBER;
        String address = "Botanická 68a";
        LocalDate birthDate = LocalDate.parse("2000-02-02");
        Long id = 1L;
        UserDAO testUserDAO = new UserDAO(id, username, passwordHash, userType, address, birthDate);
        Mockito.when(userRepository.saveUser(
                anyString(),
                anyString(),
                anyString(),
                any(LocalDate.class),
                any(UserType.class))
        ).thenReturn(testUserDAO);


        assertThatCode(
                () -> {
                    UserDAO userDAO = userService.createUser(username, passwordHash, address, birthDate, userType);
                    assertThat(userDAO.getUsername()).isEqualTo(username);
                    assertThat(userDAO.getAddress()).isEqualTo(address);
                    assertThat(userDAO.getPasswordHash()).isEqualTo(passwordHash);
                    assertThat(userDAO.getBirthDate()).isEqualTo(birthDate);
                    assertThat(userDAO.getUserType()).isEqualTo(userType);
                    assertThat(userDAO.getId()).isEqualTo(id);
                }
        ).doesNotThrowAnyException();

        verify(userRepository, times(1))
                .saveUser(anyString(),
                        anyString(),
                        anyString(),
                        any(LocalDate.class),
                        any(UserType.class));
    }

    @Test
    void createUser_usernameExists_throwsUsernameAlreadyExistsException() {
        String username = "programmer123";
        String passwordHash = "passwordHash";
        UserType userType = UserType.MEMBER;
        String address = "Botanická 68a";
        LocalDate birthDate = LocalDate.parse("2000-02-02");
        Long id = 1L;
        UserDAO testUserDAO = new UserDAO(id, username, passwordHash, userType, address, birthDate);
        Mockito.when(userRepository.findUserByUsername(anyString())).thenReturn(testUserDAO);

        assertThrows(UsernameAlreadyExistsException.class,
                () -> userService.createUser(username, passwordHash, address, birthDate, userType));

        verify(userRepository, times(0))
                .saveUser(anyString(),
                        anyString(),
                        anyString(),
                        any(LocalDate.class),
                        any(UserType.class));
    }

    @Test
    void deleteById_singleUserDelete_callsUserRepositoryDelete() {
        // Arrange
        Long idToDelete = 1L;

        // Act
        userService.deleteById(idToDelete);

        // Assert
        verify(userRepository, times(1)).deleteById(idToDelete);
    }
}