package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.exceptions.UnauthorisedException;
import cz.muni.fi.pa165.exceptions.UsernameAlreadyExistsException;
import cz.muni.fi.pa165.facade.UserFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.UserDTO;
import org.openapitools.model.UserType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

    @Mock
    UserFacade userFacade;

    @InjectMocks
    UserRestController userRestController;

    @Test
    void findById_userFound_returnsUserAndOkStatus() {
        String username = "programmer123";
        UserType userType = UserType.MEMBER;
        String address = "Botanická 68a";
        LocalDate birthDate = LocalDate.parse("2000-02-02");
        UserDTO user = new UserDTO().username(username).userType(userType).address(address).birthDate(birthDate);

        Mockito.when(userFacade.findById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<UserDTO> response = userRestController.getUser(1L);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(user);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void findById_userNotFound_returnsNotFoundStatus() {
        Mockito.when(userFacade.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<UserDTO> response = userRestController.getUser(1L);

        assertThat(response.getBody()).isNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void createUser_usernameNotExists_returnsNewUserAndCreatedStatus() {
        String username = "programmer123";
        String password = "password";
        UserType userType = UserType.MEMBER;
        String address = "Botanická 68a";
        LocalDate birthDate = LocalDate.parse("2000-02-02");
        UserDTO user = new UserDTO().username(username).userType(userType).birthDate(birthDate).address(address);

        Mockito.when(userFacade.createUser(username, password, address, birthDate, userType)).thenReturn(user);

        ResponseEntity<UserDTO> response = userRestController.createUser(username, password, address, birthDate, userType);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(user);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createUser_usernameExists_returnsUnprocessableEntityStatus() {
        String username = "programmer123";
        String password = "password";
        UserType userType = UserType.MEMBER;
        String address = "Botanická 68a";
        LocalDate birthDate = LocalDate.parse("2000-02-02");

        Mockito.when(userFacade.createUser(username, password, address, birthDate, userType)).thenThrow(UsernameAlreadyExistsException.class);

        ResponseEntity<UserDTO> response = userRestController.createUser(username, password, address, birthDate, userType);

        assertThat(response.getBody()).isNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    void deleteById_singleUserDelete_callsUserRepositoryDelete() {
        Long idToDelete = 1L;

        userRestController.deleteUser(idToDelete);

        verify(userFacade, times(1)).deleteById(idToDelete);
    }

    @Test
    void updateUser_incorrectCredentialsOrNoRights_returnsUnauthorizedStatus() {
        String username = "programmer123";
        String password = "password";
        UserType userType = UserType.MEMBER;
        String address = "Botanická 68a";
        LocalDate birthDate = LocalDate.parse("2000-02-02");
        Long id = 1L;

        Mockito.when(userFacade.updateUser(id, username, password, address, birthDate, userType)).thenThrow(UnauthorisedException.class);

        ResponseEntity<UserDTO> response = userRestController.updateUser(id, username, password, address, birthDate, userType);
        assertThat(response.getBody()).isNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void updateUser_userUpdatesNotExistingUser_returnsNotFoundStatus() {
        String username = "programmer123";
        String password = "password";
        UserType userType = UserType.MEMBER;
        String address = "Botanická 68a";
        LocalDate birthDate = LocalDate.parse("2000-02-02");
        Long id = 1L;

        Mockito.when(userFacade.updateUser(id, username, password, address, birthDate, userType)).thenReturn(Optional.empty());

        ResponseEntity<UserDTO> response = userRestController.updateUser(id, username, password, address, birthDate, userType);
        assertThat(response.getBody()).isNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateUser_sufficientRightsAndUserExists_returnsUserAndOkStatus() {
        String username = "programmer123";
        String password = "password";
        UserType userType = UserType.MEMBER;
        String address = "Botanická 68a";
        LocalDate birthDate = LocalDate.parse("2000-02-02");
        Long id = 1L;
        UserDTO user = new UserDTO().username(username).userType(userType).address(address).birthDate(birthDate);

        Mockito.when(userFacade.updateUser(id, username, password, address, birthDate, userType)).thenReturn(Optional.of(user));

        ResponseEntity<UserDTO> response = userRestController.updateUser(id, username, password, address, birthDate, userType);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(user);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void findAll_userTypeMember_returnsUsersAndOkStatus() {
        String username = "programmer123";
        UserType userType = UserType.MEMBER;
        String address = "Botanická 68a";
        LocalDate birthDate = LocalDate.parse("2000-02-02");
        UserDTO user = new UserDTO().username(username).userType(userType).address(address).birthDate(birthDate);
        List<UserDTO> users = new ArrayList<>();
        users.add(user);

        Mockito.when(userFacade.findAll(any(UserType.class))).thenReturn(users);

        ResponseEntity<List<UserDTO>> response = userRestController.getUsers(UserType.MEMBER);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(users);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void findAllAdults_returnsUsersAndOkStatus() {
        String username = "programmer123";
        UserType userType = UserType.MEMBER;
        String address = "Botanická 68a";
        LocalDate birthDate = LocalDate.parse("2000-02-02");
        UserDTO user = new UserDTO().username(username).userType(userType).address(address).birthDate(birthDate);
        List<UserDTO> users = new ArrayList<>();
        users.add(user);

        Mockito.when(userFacade.findAllAdults()).thenReturn(users);

        ResponseEntity<List<UserDTO>> response = userRestController.getAdultUsers();

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isEqualTo(users);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}