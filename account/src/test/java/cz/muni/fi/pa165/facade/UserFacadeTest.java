package cz.muni.fi.pa165.facade;

import cz.muni.fi.pa165.data.model.UserDAO;
import cz.muni.fi.pa165.service.UserService;
import cz.muni.fi.pa165.util.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.UserDTO;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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


    // Will be replaced by mapper in the future
    private UserDTO convertToDTO(UserDAO userDAO) {
        return new UserDTO()
                .username(userDAO.getUsername())
                .address(userDAO.getAddress())
                .birthDate(userDAO.getBirthDate())
                .userType(userDAO.getUserType());
    }

}