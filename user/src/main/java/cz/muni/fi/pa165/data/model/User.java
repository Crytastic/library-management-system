package cz.muni.fi.pa165.data.model;

import jakarta.persistence.*;
import org.openapitools.model.UserType;

import java.time.LocalDate;
import java.util.Objects;

/**
 * This class encapsulates information about a user.
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

    public User() {
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(passwordHash, user.passwordHash) &&
                userType == user.userType &&
                Objects.equals(address, user.address) &&
                Objects.equals(birthDate, user.birthDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, passwordHash, userType, address, birthDate);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username +
                ", passwordHash='" + passwordHash +
                ", address='" + address +
                ", userType=" + userType +
                ", birthDate=" + birthDate +
                '}';
    }
}
