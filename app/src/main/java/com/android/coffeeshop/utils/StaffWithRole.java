package com.android.coffeeshop.utils;

import androidx.room.ColumnInfo;

public class StaffWithRole {
    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "full_name")
    private String fullName;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "salary")
    private double salary;

    @ColumnInfo(name = "role_name")
    private String roleName;

    // Constructor
    public StaffWithRole(int userId, String fullName, String email, double salary, String roleName) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.salary = salary;
        this.roleName = roleName;
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public double getSalary() {
        return salary;
    }

    public String getRoleName() {
        return roleName;
    }

}