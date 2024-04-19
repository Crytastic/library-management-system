package cz.muni.fi.pa165.data.model;

import jakarta.persistence.*;
import org.openapitools.model.UserType;

import java.time.LocalDate;

/**
 * This class encapsulates information about a user account.
 *
 * @author Sophia Zápotočná
 */
@Entity
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id")
    private Long id;
    @Column(unique = true)
    private String username;
    @Column
    private String passwordHash;
    @Column
    private String address;
    @Column
    @Enumerated(EnumType.STRING)
    private UserType userType;
    @Column
    private LocalDate birthDate;

    public User() {}
    public User(String username, String passwordHash, UserType userType, String address, LocalDate birthDate) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.userType = userType;
        this.address = address;
        this.birthDate = birthDate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
