package cz.muni.fi.pa165.config;

import cz.muni.fi.pa165.data.model.Borrowing;
import cz.muni.fi.pa165.data.repository.BorrowingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InsertInitialDataService {

    private final BorrowingRepository borrowingRepository;

    @Autowired
    public InsertInitialDataService(BorrowingRepository borrowingRepository) {
        this.borrowingRepository = borrowingRepository;
    }
}
