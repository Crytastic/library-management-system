package cz.muni.fi.pa165.stubs;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Custom stub of BorrowingService
 *
 * @author Martin Suchánek
 */
@Service
public class BorrowingServiceStub {
    public List<String> apiCallToBorrowingServiceToFindBookBorrowings(Long id) {
        if (id == 1) {
            return new ArrayList<>(Arrays.asList("Borrowing1", "Borrowing2"));
        } else if (id == 2) {
            return new ArrayList<>(List.of("Borrowing3"));
        } else {
            return new ArrayList<>();
        }
    }
}
