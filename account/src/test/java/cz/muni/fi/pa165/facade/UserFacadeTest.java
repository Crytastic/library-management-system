package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.data.model.User;
import cz.muni.fi.pa165.exceptions.UnauthorisedException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Martin Suchánek
 */
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

        Optional<UserDTO> userDAO = userFacade.findById(1L);

        assertThat(userDAO).isEmpty();
    }

    @Test
    void createUser_usernameNotExists_callsUserServiceCreateUserAndReturnsNewUser() {
        String username = "programmer123";
        String passwordHash = "passwordHash";
        UserType userType = UserType.MEMBER;
        String address = "Botanická 68a";
        LocalDate birthDate = LocalDate.parse("2000-02-02");
        Long id = 1L;
        User testUserDAO = new User(id, username, passwordHash, userType, address, birthDate);
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

    @Test
    void deleteById_singleUserDelete_callsUserServiceDelete() {
        Long idToDelete = 1L;

        userFacade.deleteById(idToDelete);

        verify(userService, times(1)).deleteById(idToDelete);
    }

    @Test
    void updateUser_incorrectUsername_throwsUnauthorisedException() {
        User testUserDAO = TestDataFactory.firstMemberDAO;
        Mockito.when(userService.updateUser(
                TestDataFactory.secondMemberDAO.getId(),
                "IncorrectUserName",
                testUserDAO.getPasswordHash(),
                "Nová Adresa 123, Brno",
                null,
                null)).thenThrow(UnauthorisedException.class);

        assertThrows(UnauthorisedException.class, () ->
                userFacade.updateUser(
                        TestDataFactory.secondMemberDAO.getId(),
                        "IncorrectUserName",
                        testUserDAO.getPasswordHash(),
                        "Nová Adresa 123, Brno",
                        null,
                        null));
    }

    @Test
    void updateUser_incorrectPassword_throwsUnauthorisedException() {
        User testUserDAO = TestDataFactory.firstMemberDAO;
        Mockito.when(userService.updateUser(TestDataFactory.secondMemberDAO.getId(), testUserDAO.getUsername(), "incorrectPassword", "Nová Adresa 123, Brno", null, null)).thenThrow(UnauthorisedException.class);

        assertThrows(UnauthorisedException.class, () ->
                userFacade.updateUser(TestDataFactory.secondMemberDAO.getId(), testUserDAO.getUsername(), "incorrectPassword", "Nová Adresa 123, Brno", null, null));
    }

    @Test
    void updateUser_userUpdatesNotExistingUser_returnsEmptyOptional() {
        User actor = TestDataFactory.firstLibrarianDAO;
        String actorPassword = TestDataFactory.firstLibrarianDAOPassword;
        Long notExistingId = 20L;

        Mockito.when(userService.updateUser(notExistingId, actor.getUsername(), actorPassword, "Nová Adresa 123, Brno", null, null)).thenReturn(Optional.empty());

        assertThat(userFacade.updateUser(notExistingId, actor.getUsername(), actorPassword, "Nová Adresa 123, Brno", null, null)).isEmpty();
    }

    @Test
    void updateUser_librarianUpdatesExistingUser_returnsUpdatedUser() {
        User actor = TestDataFactory.firstLibrarianDAO;
        String actorPassword = TestDataFactory.firstLibrarianDAOPassword;

        User userToBeUpdated = TestDataFactory.firstLibrarianDAO;
        User updatedUser = TestDataFactory.firstLibrarianDAO;
        updatedUser.setAddress("Nová Adresa 132, Brno");
        updatedUser.setBirthDate(LocalDate.parse("1999-12-12"));

        Mockito.when(userService.updateUser(userToBeUpdated.getId(), actor.getUsername(), actorPassword, updatedUser.getAddress(), updatedUser.getBirthDate(), null)).thenReturn(Optional.of(updatedUser));

        assertThat(userFacade.updateUser(userToBeUpdated.getId(), actor.getUsername(), actorPassword,
                updatedUser.getAddress(), updatedUser.getBirthDate(), null)).isPresent().contains(convertToDTO(updatedUser));
    }

    @Test
    void updateUser_memberUpdatesHimself_returnsUpdatedUser() {
        User actor = TestDataFactory.firstMemberDAO;
        String actorPassword = TestDataFactory.firstMemberDAOPassword;

        User userToBeUpdated = TestDataFactory.firstMemberDAO;
        User updatedUser = TestDataFactory.firstMemberDAO;
        updatedUser.setAddress("Nová Adresa 132, Brno");
        updatedUser.setBirthDate(LocalDate.parse("1999-12-12"));

        Mockito.when(userService.updateUser(userToBeUpdated.getId(), actor.getUsername(), actorPassword, updatedUser.getAddress(), updatedUser.getBirthDate(), null)).thenReturn(Optional.of(updatedUser));

        assertThat(userFacade.updateUser(userToBeUpdated.getId(), actor.getUsername(), actorPassword,
                updatedUser.getAddress(), updatedUser.getBirthDate(), null)).isPresent().contains(convertToDTO(updatedUser));
    }

    @Test
    void updateUser_memberUpdatesTypeOnHimself_throwsUnauthorizedException() {
        User actor = TestDataFactory.firstMemberDAO;
        String actorPassword = TestDataFactory.firstMemberDAOPassword;

        User updatedUser = TestDataFactory.secondMemberDAO;
        updatedUser.setUserType(UserType.LIBRARIAN);

        Mockito.when(userService.updateUser(actor.getId(), actor.getUsername(), actorPassword,
                updatedUser.getAddress(), updatedUser.getBirthDate(), updatedUser.getUserType())).thenThrow(UnauthorisedException.class);

        assertThrows(UnauthorisedException.class, () -> userFacade.updateUser(actor.getId(), actor.getUsername(), actorPassword,
                updatedUser.getAddress(), updatedUser.getBirthDate(), updatedUser.getUserType()));
    }

    @Test
    void updateUser_memberUpdatesOtherUser_throwsUnauthorizedException() {
        User actor = TestDataFactory.firstMemberDAO;
        String actorPassword = TestDataFactory.firstMemberDAOPassword;

        User userToBeUpdated = TestDataFactory.secondMemberDAO;
        User updatedUser = TestDataFactory.secondMemberDAO;
        updatedUser.setAddress("Nová Adresa 132, Brno");
        updatedUser.setBirthDate(LocalDate.parse("1999-12-12"));

        Mockito.when(userService.updateUser(userToBeUpdated.getId(), actor.getUsername(), actorPassword,
                updatedUser.getAddress(), updatedUser.getBirthDate(), null)).thenThrow(UnauthorisedException.class);

        assertThrows(UnauthorisedException.class, () -> userFacade.updateUser(userToBeUpdated.getId(), actor.getUsername(), actorPassword,
                updatedUser.getAddress(), updatedUser.getBirthDate(), null));
    }

    @Test
    void findAll_userTypeMember_callsUserServiceFindAllAndReturnsUsers() {
        List<User> users = new ArrayList<>();
        users.add(TestDataFactory.firstMemberDAO);
        users.add(TestDataFactory.secondMemberDAO);

        Mockito.when(userService.findAll(UserType.MEMBER)).thenReturn(users);

        assertThat(userFacade.findAll(UserType.MEMBER))
                .isNotNull()
                .hasSize(2)
                .containsExactlyInAnyOrder(convertToDTO(TestDataFactory.firstMemberDAO), convertToDTO(TestDataFactory.secondMemberDAO));
        verify(userService, times(1)).findAll(UserType.MEMBER);
    }

    @Test
    void findAllAdults_callsUserServiceFindAllAdultsAndReturnsUsers() {
        List<User> users = new ArrayList<>();
        users.add(TestDataFactory.firstMemberDAO);
        users.add(TestDataFactory.secondMemberDAO);

        Mockito.when(userService.findAllAdults()).thenReturn(users);

        assertThat(userFacade.findAllAdults())
                .isNotNull()
                .hasSize(2)
                .containsExactlyInAnyOrder(convertToDTO(TestDataFactory.firstMemberDAO), convertToDTO(TestDataFactory.secondMemberDAO));
        verify(userService, times(1)).findAllAdults();
    }

    // Will be replaced by mapper in the future
    private UserDTO convertToDTO(User userDAO) {
        return new UserDTO()
                .id(userDAO.getId())
                .username(userDAO.getUsername())
                .address(userDAO.getAddress())
                .birthDate(userDAO.getBirthDate())
                .userType(userDAO.getUserType());
    }

}