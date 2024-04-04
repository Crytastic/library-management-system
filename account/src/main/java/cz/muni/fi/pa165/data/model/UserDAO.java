package cz.muni.fi.pa165.data.model;

import org.openapitools.model.UserType;

import java.time.LocalDate;

public class UserDAO {

    private Long id;

    private String username;

    private String passwordHash;

    private String address;

    private UserType userType;

    private LocalDate birthDate;

    public UserDAO(String username, String passwordHash, UserType userType, String address, LocalDate birthDate) {
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
}
