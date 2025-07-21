package com.android.coffeeshop.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.android.coffeeshop.entity.UserRole;

public interface UserRoleDao {
    void insertUserRole(UserRole userRole);
}
