package cz.muni.fi.pa165.rest;

import cz.muni.fi.pa165.data.model.User;
import cz.muni.fi.pa165.data.repository.UserRepository;
import cz.muni.fi.pa165.exceptionhandling.ApiError;
import cz.muni.fi.pa165.util.ObjectConverter;
import org.junit.jupiter.api.Test;
import org.openapitools.model.UserDTO;
import org.openapitools.model.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Sophia Zápotočná
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserRestControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void getUser_validId_returnsUser() throws Exception {
        Long id = 1L;
        String username = "Filip";
        String address = "Botanická 18";
        String passHash = "pskdycbd5s";
        LocalDate birthDate = LocalDate.of(2000, 12, 12);
        UserType userType = UserType.LIBRARIAN;
        User user = new User(username, passHash, userType, address, birthDate);
        user.setId(id);
        userRepository.save(user);

        String responseJson = mockMvc.perform(get("/api/users/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        UserDTO response = ObjectConverter.convertJsonToObject(responseJson, UserDTO.class);

        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getUsername()).isEqualTo(username);
        assertThat(response.getUserType()).isEqualTo(userType);
        assertThat(response.getAddress()).isEqualTo(address);
        assertThat(response.getBirthDate()).isEqualTo(birthDate);
    }

    @Test
    void getUser_invalidId_returnsNotFound() throws Exception {
        Long id = 11L;

        String responseJson = mockMvc.perform(get("/api/users/{id}", id)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        ApiError apiError = ObjectConverter.convertJsonToObject(responseJson, ApiError.class);

        assertThat(apiError.getMessage()).isEqualTo(String.format("User with id: %d not found", id));
    }
}
