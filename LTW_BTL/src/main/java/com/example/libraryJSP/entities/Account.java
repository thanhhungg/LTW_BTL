package com.example.libraryJSP.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "username")
    @NotBlank(message = "Enter your Username!")
    private String username ;
    @Column(name = "password")
    @NotBlank(message = "Enter your Password!")
    private String password;

    @Column(name = "name")
    @NotBlank(message = "Enter your Name!")
    private String name;

    @Column(name = "email")
    @NotBlank(message = "Enter your Email!")
    private String email;

    @Column(name = "isadmin")
    private int isadmin;
    public Account() {
    }

    public Account(int id, String username, String password, String name, String email,int isadmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.isadmin = isadmin;
    }

    public Account (int isadmin){
        this.isadmin=isadmin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(int isadmin) {
        this.isadmin = isadmin;
    }
}
