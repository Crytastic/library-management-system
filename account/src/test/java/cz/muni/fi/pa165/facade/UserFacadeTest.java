package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.data.model.UserDAO;
import cz.muni.fi.pa165.exceptions.UsernameAlreadyExistsException;
import cz.muni.fi.pa165.service.UserService;
import cz.muni.fi.pa165.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.UserDTO;
import org.openapitools.model.UserType;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {
    @Mock
    UserService userService;

    @InjectMocks
    UserFacade userFacade;

    @Test
    void findById_userFound_returnsUser() {
        Mockito.when(userService.findById(1L)).thenReturn(Optional.ofNullable(TestDataFactory.firstMemberDAO));

        Optional<UserDTO> userDTO = userFacade.findById(1L);

        assertThat(userDTO).isPresent();
        assertThat(userDTO.get()).isEqualTo(convertToDTO(TestDataFactory.firstMemberDAO));

    }

    @Test
    void findById_userNotFound_returnsEmptyOptional() {
        Mockito.when(userService.findById(1L)).thenReturn(Optional.empty());

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
        Mockito.when(userService.createUser(
                anyString(),
                anyString(),
                anyString(),
                any(LocalDate.class),
                any(UserType.class))
        ).thenReturn(testUserDAO);


        UserDTO userDTO = userFacade.createUser(username, passwordHash, address, birthDate, userType);
        assertThat(userDTO.getUsername()).isEqualTo(username);
        assertThat(userDTO.getAddress()).isEqualTo(address);
        assertThat(userDTO.getBirthDate()).isEqualTo(birthDate);
        assertThat(userDTO.getUserType()).isEqualTo(userType);

        verify(userService, times(1))
                .createUser(anyString(),
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

        Mockito.when(userService.createUser(anyString(), anyString(), anyString(), any(LocalDate.class), any(UserType.class))).thenThrow(UsernameAlreadyExistsException.class);

        assertThrows(UsernameAlreadyExistsException.class,
                () -> userFacade.createUser(username, passwordHash, address, birthDate, userType));
    }

    // Will be replaced by mapper in the future
    private UserDTO convertToDTO(UserDAO userDAO) {
        return new UserDTO()
                .username(userDAO.getUsername())
                .address(userDAO.getAddress())
                .birthDate(userDAO.getBirthDate())
                .userType(userDAO.getUserType());
    }

}