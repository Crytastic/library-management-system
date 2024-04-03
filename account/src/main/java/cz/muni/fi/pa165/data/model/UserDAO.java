package cz.muni.fi.pa165.data.model;

import java.time.LocalDate;

public class UserDAO {

    private Long id;

    private String username;

    private String address;

    private String userType;

    private LocalDate birthDate;

    public UserDAO(String username, String accountType, String address, LocalDate birthDate) {
        this.username = username;
        this.userType = accountType;
        this.address = address;
        this.birthDate = birthDate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserType(String userType) {
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

    public String getUserType() {
        return userType;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}
