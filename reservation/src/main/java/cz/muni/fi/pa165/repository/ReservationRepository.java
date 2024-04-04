package cz.muni.fi.pa165.repository;

import cz.muni.fi.pa165.dao.ReservationDAO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class ReservationRepository {
    private final HashMap<Long, ReservationDAO> reservations = new HashMap<>();
    private static Long index = 1L;

    public List<ReservationDAO> findAll() {
        return reservations.values().stream().toList();
    }

}
