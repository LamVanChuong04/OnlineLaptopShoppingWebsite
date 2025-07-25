package com.fia.project_ecommerce.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.fia.project_ecommerce.controller.service.valid.RegisterChecked;

@RegisterChecked
public class RegisterDTO {
    @NotNull
    @Size(min = 3, message = "Firstname phải có tối thiểu 3 ký tự")
    private String firstName;

    @NotNull
    @Size(min = 3, message = "Lastname phải có tối thiểu 3 ký tự")
    private String lastName;
    @Email(message = "Email không hợp lệ", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @Size(min = 3, message = "Password phải có tối thiểu 6 ký tự")
    private String password;
    private String confirmPassword;
    private String address;
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
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
    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
       
}
