package com.android.coffeeshop.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.android.coffeeshop.entity.UserRole;

@Dao
public interface UserRoleDao {
    @Insert
    void insertUserRole(UserRole userRole);
}
