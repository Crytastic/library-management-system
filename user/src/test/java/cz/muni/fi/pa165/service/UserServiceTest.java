package cz.muni.fi.pa165.service;

import cz.muni.fi.pa165.data.model.User;
import cz.muni.fi.pa165.data.repository.UserRepository;
import cz.muni.fi.pa165.exceptionhandling.exceptions.ResourceAlreadyExistsException;
import cz.muni.fi.pa165.exceptionhandling.exceptions.ResourceNotFoundException;
import cz.muni.fi.pa165.exceptionhandling.exceptions.UnauthorizedException;
import cz.muni.fi.pa165.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.UserType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Martin Suchánek
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void findById_userFound_returnsUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(TestDataFactory.firstMemberDAO));

        User userDAO = userService.findById(1L);

        assertThat(userDAO).isEqualTo(TestDataFactory.firstMemberDAO);
    }

    @Test
    void findById_userNotFound_throwsResourceNotFoundException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Throwable exception = assertThrows(ResourceNotFoundException.class, () -> userService.findById(1L));

        assertThat(exception.getMessage()).isEqualTo(String.format("User with id: %d not found", 1L));
    }

    @Test
    void createUser_usernameNotExists_callsUserRepositorySaveUserAndReturnsNewUser() {
        String username = "programmer123";
        String passwordHash = "passwordHash";
        UserType userType = UserType.MEMBER;
        String address = "Botanická 68a";
        LocalDate birthDate = LocalDate.parse("2000-02-02");
        User testUserDAO = new User(username, passwordHash, userType, address, birthDate);
        when(userRepository.save(any(User.class))).thenReturn(testUserDAO);

        User userDAO = userService.createUser(username, passwordHash, address, birthDate, userType);
        assertThat(userDAO.getUsername()).isEqualTo(username);
        assertThat(userDAO.getAddress()).isEqualTo(address);
        assertThat(userDAO.getPasswordHash()).isEqualTo(passwordHash);
        assertThat(userDAO.getBirthDate()).isEqualTo(birthDate);
        assertThat(userDAO.getUserType()).isEqualTo(userType);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_usernameExists_notCallsUserRepositorySaveUserAndThrowsUsernameAlreadyExistsException() {
        String username = "programmer123";
        String passwordHash = "passwordHash";
        UserType userType = UserType.MEMBER;
        String address = "Botanická 68a";
        LocalDate birthDate = LocalDate.parse("2000-02-02");
        when(userRepository.findUserByUsername(username)).thenReturn(new User(username, passwordHash, userType, address, birthDate));

        assertThrows(ResourceAlreadyExistsException.class,
                () -> userService.createUser(username, passwordHash, address, birthDate, userType));
    }

    @Test
    void deleteById_singleUserDelete_callsUserRepositoryDelete() {
        Long idToDelete = 1L;

        userService.deleteById(idToDelete);

        verify(userRepository, times(1)).deleteById(idToDelete);
    }

    @Test
    void deleteAll_allUsersDelete_callsUserRepositoryDeleteAll() {
        userService.deleteAll();

        verify(userRepository, times(1)).deleteAll();
    }

    @Test
    void updateUser_incorrectUsername_notCallsUserRepositoryUpdateUserAndThrowsUnauthorisedException() {
        User testUser = TestDataFactory.firstMemberDAO;
        when(userRepository.findUserByUsername(anyString())).thenReturn(null);

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
                userService.updateUser(TestDataFactory.secondMemberDAO.getId(), "IncorrectUserName", testUser.getPasswordHash(), "Nová Adresa 123, Brno", null, null));

        assertThat(exception.getMessage()).isEqualTo("Combination of username and password does not exist.");
        verify(userRepository, times(0)).save(testUser);
    }

    @Test
    void updateUser_incorrectPassword_notCallsUserRepositoryUpdateUserAndThrowsUnauthorisedException() {
        User testUser = TestDataFactory.firstMemberDAO;
        when(userRepository.findUserByUsername(anyString())).thenReturn(TestDataFactory.secondMemberDAO);

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
                userService.updateUser(TestDataFactory.secondMemberDAO.getId(), testUser.getUsername(), "incorrectPassword", "Nová Adresa 123, Brno", null, null));

        assertThat(exception.getMessage()).isEqualTo("Combination of username and password does not exist.");
        verify(userRepository, times(0)).save(testUser);
    }

    @Test
    void updateUser_userUpdatesNotExistingUser_notCallsUserRepositoryUpdateUserAndThrowsResourceNotFoundException() {
        User actor = TestDataFactory.firstLibrarianDAO;
        String actorPassword = TestDataFactory.firstLibrarianDAOPassword;
        Long notExistingId = 20L;

        when(userRepository.findUserByUsername(actor.getUsername())).thenReturn(actor);
        when(userRepository.findById(notExistingId)).thenThrow(new ResourceNotFoundException(String.format("User with id: %d not found", notExistingId)));

        Throwable exception = assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(notExistingId, actor.getUsername(), actorPassword,
                "Nová Adresa 132, Brno", null, null));

        assertThat(exception.getMessage()).isEqualTo(String.format("User with id: %d not found", notExistingId));

        verify(userRepository, times(1)).findUserByUsername(actor.getUsername());
        verify(userRepository, times(1)).findById(notExistingId);
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void updateUser_librarianUpdatesExistingUser_callsMultipleUserRepositoryMethodsAndReturnsUpdatedUser() {
        User actor = TestDataFactory.firstLibrarianDAO;
        String actorPassword = TestDataFactory.firstLibrarianDAOPassword;

        User userToBeUpdated = TestDataFactory.firstLibrarianDAO;
        User updatedUser = TestDataFactory.firstLibrarianDAO;
        updatedUser.setAddress("Nová Adresa 132, Brno");
        updatedUser.setBirthDate(LocalDate.parse("1999-12-12"));

        when(userRepository.findUserByUsername(actor.getUsername())).thenReturn(actor);
        when(userRepository.findById(userToBeUpdated.getId())).thenReturn(Optional.of(userToBeUpdated));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        assertThat(userService.updateUser(userToBeUpdated.getId(), actor.getUsername(), actorPassword,
                updatedUser.getAddress(), updatedUser.getBirthDate(), null)).isEqualTo(updatedUser);

        verify(userRepository, times(1)).findUserByUsername(actor.getUsername());
        verify(userRepository, times(1)).findById(userToBeUpdated.getId());
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void updateUser_memberUpdatesHimself_callsMultipleUserRepositoryMethodsAndReturnsUpdatedUser() {
        User actor = TestDataFactory.firstMemberDAO;
        String actorPassword = TestDataFactory.firstMemberDAOPassword;

        User userToBeUpdated = TestDataFactory.firstMemberDAO;
        User updatedUser = TestDataFactory.firstMemberDAO;
        updatedUser.setAddress("Nová Adresa 132, Brno");
        updatedUser.setBirthDate(LocalDate.parse("1999-12-12"));

        when(userRepository.findUserByUsername(actor.getUsername())).thenReturn(actor);
        when(userRepository.findById(userToBeUpdated.getId())).thenReturn(Optional.of(userToBeUpdated));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        assertThat(userService.updateUser(userToBeUpdated.getId(), actor.getUsername(), actorPassword,
                updatedUser.getAddress(), updatedUser.getBirthDate(), null)).isEqualTo(updatedUser);

        verify(userRepository, times(1)).findUserByUsername(actor.getUsername());
        verify(userRepository, times(1)).findById(userToBeUpdated.getId());
        verify(userRepository, times(1)).save(updatedUser);
    }

    @Test
    void updateUser_memberUpdatesTypeOnHimself_callsOnlyUserRepositoryFindUserByUsernameAndThrowsUnauthorizedException() {
        User actor = TestDataFactory.firstMemberDAO;
        String actorPassword = TestDataFactory.firstMemberDAOPassword;

        User userToBeUpdated = TestDataFactory.secondMemberDAO;
        User updatedUser = TestDataFactory.secondMemberDAO;
        updatedUser.setUserType(UserType.LIBRARIAN);

        when(userRepository.findUserByUsername(actor.getUsername())).thenReturn(actor);

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () ->
                userService.updateUser(userToBeUpdated.getId(), actor.getUsername(), actorPassword,
                updatedUser.getAddress(), updatedUser.getBirthDate(), updatedUser.getUserType()));

        assertThat(exception.getMessage())
                .isEqualTo("As a MEMBER you cannot update other users or update your userType.");

        verify(userRepository, times(1)).findUserByUsername(actor.getUsername());
        verify(userRepository, times(0)).findById(userToBeUpdated.getId());
        verify(userRepository, times(0)).save(updatedUser);
    }

    @Test
    void updateUser_memberUpdatesOtherUser_callsOnlyUserRepositoryFindUserByUsernameAndThrowsUnauthorizedException() {
        User actor = TestDataFactory.firstMemberDAO;
        String actorPassword = TestDataFactory.firstMemberDAOPassword;

        User userToBeUpdated = TestDataFactory.secondMemberDAO;
        User updatedUser = TestDataFactory.secondMemberDAO;
        updatedUser.setAddress("Nová Adresa 132, Brno");
        updatedUser.setBirthDate(LocalDate.parse("1999-12-12"));

        when(userRepository.findUserByUsername(actor.getUsername())).thenReturn(actor);

        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> userService.updateUser(userToBeUpdated.getId(), actor.getUsername(), actorPassword,
                updatedUser.getAddress(), updatedUser.getBirthDate(), null));

        assertThat(exception.getMessage())
                .isEqualTo("As a MEMBER you cannot update other users or update your userType.");

        verify(userRepository, times(1)).findUserByUsername(actor.getUsername());
        verify(userRepository, times(0)).findById(userToBeUpdated.getId());
        verify(userRepository, times(0)).save(updatedUser);
    }

    @Test
    void findAll_userTypeMember_callsUserRepositoryFindAllAndReturnsUsers() {
        List<User> users = new ArrayList<>();
        users.add(TestDataFactory.firstMemberDAO);
        users.add(TestDataFactory.secondMemberDAO);

        when(userRepository.findAllByUserType(UserType.MEMBER)).thenReturn(users);

        assertThat(userService.findAll(UserType.MEMBER))
                .isNotNull()
                .hasSize(2)
                .containsExactlyInAnyOrder(TestDataFactory.firstMemberDAO, TestDataFactory.secondMemberDAO);
        verify(userRepository, times(1)).findAllByUserType(UserType.MEMBER);
    }

    @Test
    void findAllAdults_callsUserRepositoryFindAllAdultsAndReturnsUsers() {
        List<User> users = new ArrayList<>();
        users.add(TestDataFactory.firstMemberDAO);
        users.add(TestDataFactory.secondMemberDAO);
        LocalDate dateOfAdultAge = LocalDate.now().minusYears(18);

        when(userRepository.findAllByAge(dateOfAdultAge)).thenReturn(users);

        assertThat(userService.findAllAdults())
                .isNotNull()
                .hasSize(2)
                .containsExactlyInAnyOrder(TestDataFactory.firstMemberDAO, TestDataFactory.secondMemberDAO);
        verify(userRepository, times(1)).findAllByAge(dateOfAdultAge);
    }
}
