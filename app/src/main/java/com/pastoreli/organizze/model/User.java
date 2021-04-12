package com.pastoreli.organizze.model;

import com.google.firebase.database.Exclude;

public class User {

    private String idUser;
    private String name;
    private String email;
    private String password;
    private Double totalRevenue = 0.00;
    private Double totalExpenditure = 0.00;

    public User() {
    }

    @Exclude
    public String getIdUser() { return idUser; }

    public void setIdUser(String idUser) { this.idUser = idUser; }

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

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Double getTotalExpenditure() {
        return totalExpenditure;
    }

    public void setTotalExpenditure(Double totalExpenditure) {
        this.totalExpenditure = totalExpenditure;
    }
}
