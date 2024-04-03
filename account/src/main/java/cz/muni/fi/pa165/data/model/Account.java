package cz.muni.fi.pa165.data.model;

import java.time.LocalDate;

public class Account {

    private Long id;

    private String username;

    private String accountType;

    private String address;

    private LocalDate birthDate;

    public Account(String username, String accountType, String address, LocalDate birthDate) {
        this.username = username;
        this.accountType = accountType;
        this.address = address;
        this.birthDate = birthDate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
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

    public String getAccountType() {
        return accountType;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
}
