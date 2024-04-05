package cz.muni.fi.pa165.stubs;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RentalServiceStub {
    public List<String> apiCallToRentalServiceToFindBookRentals(Long id) {
        if (id == 1) {
            return new ArrayList<>(Arrays.asList("Rental1", "Rental2"));
        } else if (id == 2) {
            return new ArrayList<>(List.of("Rental3"));
        } else {
            return new ArrayList<>();
        }
    }
}
