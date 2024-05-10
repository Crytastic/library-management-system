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
public class ReservationSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthorizedAccessToGetReservations_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void unauthorizedAccessToPostReservations_returnsForbidden() throws Exception {
        mockMvc.perform(post("/api/reservations"))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthorizedAccessToDeleteReservations_returnsForbidden() throws Exception {
        mockMvc.perform(delete("/api/reservations"))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthorizedAccessToGetReservationById_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void unauthorizedAccessToPatchReservationById_returnsForbidden() throws Exception {
        mockMvc.perform(patch("/api/reservations/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthorizedAccessToDeleteReservationById_returnsForbidden() throws Exception {
        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void unauthorizedAccessToGetActiveReservations_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/reservations/active"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void unauthorizedAccessToGetExpiredReservations_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/reservations/expired"))
                .andExpect(status().isUnauthorized());
    }
}
