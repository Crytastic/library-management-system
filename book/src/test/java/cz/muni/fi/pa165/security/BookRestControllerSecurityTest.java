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
public class BookRestControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthorizedAccessToGetBooks_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void unauthorizedAccessToPostBooks_returnsForbidden() throws Exception {
        mockMvc.perform(post("/api/books"))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthorizedAccessToDeleteBooks_returnsForbidden() throws Exception {
        mockMvc.perform(delete("/api/books"))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthorizedAccessToGetBookById_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void unauthorizedAccessToDeleteBookById_returnsForbidden() throws Exception {
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthorizedAccessToPatchBookById_returnsForbidden() throws Exception {
        mockMvc.perform(patch("/api/books/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthorizedAccessToGetBookBorrowings_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/books/1/borrowings"))
                .andExpect(status().isUnauthorized());
    }
}
