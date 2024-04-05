package cz.muni.fi.pa165.rest;

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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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

}