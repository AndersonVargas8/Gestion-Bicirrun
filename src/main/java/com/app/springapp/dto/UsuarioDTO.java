package com.app.springapp.dto;

import java.util.ArrayList;

public class UsuarioDTO {
    private Integer id;
    private String username;
    private String password;
    private String confirmPassword;
    private String role;
    private ArrayList<Long> estaciones;

    public UsuarioDTO(Integer id, String username, String password, String confirmPassword, String role,
            ArrayList<Long> estaciones) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.role = role;
        this.estaciones = estaciones;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ArrayList<Long> getEstaciones() {
        return estaciones;
    }

    public void setEstaciones(ArrayList<Long> estaciones) {
        this.estaciones = estaciones;
    }

    

    
}
