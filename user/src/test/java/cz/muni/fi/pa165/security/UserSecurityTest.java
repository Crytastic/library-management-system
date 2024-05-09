package cz.muni.fi.pa165.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthorizedAccessToGetUsers_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void unauthorizedAccessToCreateUser_returnsForbidden() throws Exception {
        mockMvc.perform(post("/api/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthorizedAccessToDeleteUsers_returnsForbidden() throws Exception {
        mockMvc.perform(delete("/api/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthorizedAccessToGetUserById_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void unauthorizedAccessToDeleteUserById_returnsForbidden() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthorizedAccessToUpdateUser_returnsForbidden() throws Exception {
        mockMvc.perform(patch("/api/users/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthorizedAccessToGetAdultUsers_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users/adults"))
                .andExpect(status().isUnauthorized());
    }
}
