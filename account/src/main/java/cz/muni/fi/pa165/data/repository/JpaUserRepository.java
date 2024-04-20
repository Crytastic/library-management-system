package cz.muni.fi.pa165.data.repository;

import cz.muni.fi.pa165.data.model.User;
import org.openapitools.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface JpaUserRepository extends JpaRepository<User, Long>  {
    @Query("SELECT u FROM User u WHERE :userType IS NULL OR u.userType = :userType")
    List<User> findAllByUserType(UserType userType);

    @Query("SELECT u FROM User u WHERE u.birthDate <= :olderOrEqualThan")
    List<User> findAllByAge(LocalDate olderOrEqualThan);

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findUserByUsername(String username);
}
