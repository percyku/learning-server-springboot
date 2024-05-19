package com.percyku.learningserver.learningserverspringboot.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class UserRegisterRequest {

    @NotBlank(message = "email cannot empty")
    @Email(message = "please check your email is correct or not")
    private String email;
    @NotBlank(message = "password cannot empty")
    private String password;
    @NotBlank(message = "username cannot empty")
    private String username;
    @NotBlank(message = "first_name cannot empty")
    private String first_name;
    @NotBlank(message = "last_name cannot empty")
    private String last_name;


    public UserRegisterRequest() {

    }

    @NotEmpty(message = "roles cannot empty")
    private List<String> roles;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }


    @Override
    public String toString() {
        return "Member{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", roles=" + roles +
                '}';
    }
}
