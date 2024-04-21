package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.facade.UserFacade;
import cz.muni.fi.pa165.util.ObjectConverter;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openapitools.model.UserDTO;
import org.openapitools.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserRestController.class})
public class UserRestControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserFacade userFacade;

    @Test
    void getUser_validId_returnsUser() throws Exception {
        Long id = 1L;
        String username = "Filip";
        String address = "Botanická 18";
        LocalDate birthDate = LocalDate.of(2000, 12, 12);
        UserType userType = UserType.LIBRARIAN;
        UserDTO userDTO = new UserDTO().id(id).username(username).userType(userType)
                .address(address).birthDate(birthDate);

        Mockito.when(userFacade.findById(id)).thenReturn(Optional.of(userDTO));

        String responseJson = mockMvc.perform(get("/api/users/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        UserDTO response = ObjectConverter.convertJsonToObject(responseJson, UserDTO.class);

        assertThat(responseJson).isEqualTo("{\"id\":1,\"username\":\"Filip\",\"address\":\"Botanická 18\",\"birthDate\":\"2000-12-12\",\"userType\":\"LIBRARIAN\"}");
        assertThat(response).isEqualTo(userDTO);
    }

    @Test
    void getUser_invalidId_returnsNotFound() throws Exception {
        Long id = 11L;
        Mockito.when(userFacade.findById(id)).thenReturn(Optional.empty());

        String responseJson = mockMvc.perform(get("/api/users/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(responseJson).isEqualTo("");
    }
}
