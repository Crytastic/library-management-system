package cz.muni.fi.pa165.data.repository;

import cz.muni.fi.pa165.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, Long>  {
}
