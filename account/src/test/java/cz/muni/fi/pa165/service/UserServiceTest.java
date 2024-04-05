package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.UserDAO;
import cz.muni.fi.pa165.data.repository.UserRepository;
import cz.muni.fi.pa165.exceptions.UnauthorisedException;
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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
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
        Long idToDelete = 1L;

        userService.deleteById(idToDelete);

        verify(userRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void updateUser_incorrectUsername_throwsUnauthorisedException() {
        UserDAO testUserDAO = TestDataFactory.firstMemberDAO;
        Mockito.when(userRepository.findUserByUsername(anyString())).thenReturn(null);

        assertThrows(UnauthorisedException.class, () ->
                userService.updateUser(TestDataFactory.secondMemberDAO.getId(), "IncorrectUserName",testUserDAO.getPasswordHash(),"Nová Adresa 123, Brno", null, null));

        verify(userRepository, times(0)).updateUser(TestDataFactory.secondMemberDAO.getId(), testUserDAO);

    }

    @Test
    void updateUser_incorrectPassword_throwsUnauthorisedException() {
        UserDAO testUserDAO = TestDataFactory.firstMemberDAO;
        Mockito.when(userRepository.findUserByUsername(anyString())).thenReturn(TestDataFactory.secondMemberDAO);

        assertThrows(UnauthorisedException.class, () ->
                userService.updateUser(TestDataFactory.secondMemberDAO.getId(), testUserDAO.getUsername(), "incorrectPassword","Nová Adresa 123, Brno", null, null));

        verify(userRepository, times(0)).updateUser(TestDataFactory.secondMemberDAO.getId(), testUserDAO);
    }

    @Test
    void updateUser_userUpdatesNotExistingUser_returnsEmptyOptional() {
        UserDAO actor = TestDataFactory.firstLibrarianDAO;
        String actorPassword = TestDataFactory.firstLibrarianDAOPassword;
        Long notExistingId = 20L;

        Mockito.when(userRepository.findUserByUsername(actor.getUsername())).thenReturn(actor);
        Mockito.when(userRepository.findById(notExistingId)).thenReturn(Optional.empty());

        assertThat(userService.updateUser(notExistingId, actor.getUsername(), actorPassword,
                "Nová Adresa 132, Brno", null, null)).isEmpty();

        verify(userRepository, times(1)).findUserByUsername(actor.getUsername());
        verify(userRepository, times(1)).findById(notExistingId);
        verify(userRepository, times(0)).updateUser(eq(notExistingId), any(UserDAO.class));
    }
    @Test
    void updateUser_librarianUpdatesExistingUser_returnsUpdatedUser() {
        UserDAO actor = TestDataFactory.firstLibrarianDAO;
        String actorPassword = TestDataFactory.firstLibrarianDAOPassword;

        UserDAO userToBeUpdated = TestDataFactory.firstLibrarianDAO;
        UserDAO updatedUser = TestDataFactory.firstLibrarianDAO;
        updatedUser.setAddress("Nová Adresa 132, Brno");
        updatedUser.setBirthDate(LocalDate.parse("1999-12-12"));

        Mockito.when(userRepository.findUserByUsername(actor.getUsername())).thenReturn(actor);
        Mockito.when(userRepository.findById(userToBeUpdated.getId())).thenReturn(Optional.of(userToBeUpdated));
        Mockito.when(userRepository.updateUser(userToBeUpdated.getId(), updatedUser)).thenReturn(updatedUser);

        assertThat(userService.updateUser(userToBeUpdated.getId(), actor.getUsername(), actorPassword,
                updatedUser.getAddress(), updatedUser.getBirthDate(), null)).isPresent().contains(updatedUser);

        verify(userRepository, times(1)).findUserByUsername(actor.getUsername());
        verify(userRepository, times(1)).findById(userToBeUpdated.getId());
        verify(userRepository, times(1)).updateUser(userToBeUpdated.getId(), updatedUser);
    }

    @Test
    void updateUser_memberUpdatesHimself_returnsUpdatedUser() {
        UserDAO actor = TestDataFactory.firstMemberDAO;
        String actorPassword = TestDataFactory.firstMemberDAOPassword;

        UserDAO userToBeUpdated = TestDataFactory.firstMemberDAO;
        UserDAO updatedUser = TestDataFactory.firstMemberDAO;
        updatedUser.setAddress("Nová Adresa 132, Brno");
        updatedUser.setBirthDate(LocalDate.parse("1999-12-12"));

        Mockito.when(userRepository.findUserByUsername(actor.getUsername())).thenReturn(actor);
        Mockito.when(userRepository.findById(userToBeUpdated.getId())).thenReturn(Optional.of(userToBeUpdated));
        Mockito.when(userRepository.updateUser(userToBeUpdated.getId(), updatedUser)).thenReturn(updatedUser);

        assertThat(userService.updateUser(userToBeUpdated.getId(), actor.getUsername(), actorPassword,
                updatedUser.getAddress(), updatedUser.getBirthDate(), null)).isPresent().contains(updatedUser);

        verify(userRepository, times(1)).findUserByUsername(actor.getUsername());
        verify(userRepository, times(1)).findById(userToBeUpdated.getId());
        verify(userRepository, times(1)).updateUser(userToBeUpdated.getId(), updatedUser);
    }

    @Test
    void updateUser_memberUpdatesTypeOnHimself_throwsUnauthorizedException() {
        UserDAO actor = TestDataFactory.firstMemberDAO;
        String actorPassword = TestDataFactory.firstMemberDAOPassword;

        UserDAO userToBeUpdated = TestDataFactory.secondMemberDAO;
        UserDAO updatedUser = TestDataFactory.secondMemberDAO;
        updatedUser.setUserType(UserType.LIBRARIAN);

        Mockito.when(userRepository.findUserByUsername(actor.getUsername())).thenReturn(actor);

        assertThrows(UnauthorisedException.class, () ->  userService.updateUser(userToBeUpdated.getId(), actor.getUsername(), actorPassword,
                updatedUser.getAddress(), updatedUser.getBirthDate(), updatedUser.getUserType()));

        verify(userRepository, times(1)).findUserByUsername(actor.getUsername());
        verify(userRepository, times(0)).findById(userToBeUpdated.getId());
        verify(userRepository, times(0)).updateUser(userToBeUpdated.getId(), updatedUser);
    }
    @Test
    void updateUser_memberUpdatesOtherUser_throwsUnauthorizedException() {
        UserDAO actor = TestDataFactory.firstMemberDAO;
        String actorPassword = TestDataFactory.firstMemberDAOPassword;

        UserDAO userToBeUpdated = TestDataFactory.secondMemberDAO;
        UserDAO updatedUser = TestDataFactory.secondMemberDAO;
        updatedUser.setAddress("Nová Adresa 132, Brno");
        updatedUser.setBirthDate(LocalDate.parse("1999-12-12"));

        Mockito.when(userRepository.findUserByUsername(actor.getUsername())).thenReturn(actor);

        assertThrows(UnauthorisedException.class, () ->  userService.updateUser(userToBeUpdated.getId(), actor.getUsername(), actorPassword,
                updatedUser.getAddress(), updatedUser.getBirthDate(), null));

        verify(userRepository, times(1)).findUserByUsername(actor.getUsername());
        verify(userRepository, times(0)).findById(userToBeUpdated.getId());
        verify(userRepository, times(0)).updateUser(userToBeUpdated.getId(), updatedUser);
    }

}