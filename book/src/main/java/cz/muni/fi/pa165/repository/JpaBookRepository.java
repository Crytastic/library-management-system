package cz.muni.fi.pa165.repository;

import cz.muni.fi.pa165.dao.BookDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaBookRepository extends JpaRepository<BookDAO, Long> {
}
