package cz.muni.fi.pa165.repository;

import cz.muni.fi.pa165.dao.RentalDAO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class RentalRepository {
    private final HashMap<Long, RentalDAO> rentals = new HashMap<>();
    private static Long index = 1L;

    public List<RentalDAO> findAll() {
        return rentals.values().stream().toList();
    }

    public RentalDAO store(RentalDAO rentalDAO) {
        rentalDAO.setId(index);
        rentals.put(rentalDAO.getId(), rentalDAO);
        index++;
        return rentalDAO;
    }

    public Optional<RentalDAO> findById(Long id) {
        return Optional.ofNullable(rentals.get(id));
    }

    public void deleteById(Long id) {
        rentals.remove(id);
    }

    public Optional<RentalDAO> updateById(Long id, RentalDAO rentalDAO) {
        rentals.put(id, rentalDAO);
        return Optional.ofNullable(rentalDAO);
    }
}
