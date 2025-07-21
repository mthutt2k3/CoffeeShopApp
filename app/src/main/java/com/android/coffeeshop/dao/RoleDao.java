package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.android.coffeeshop.entity.Role;

import java.util.List;

@Dao
public interface RoleDao {

    void insertRole(Role role);

    Role getRoleByName(String roleName);

    LiveData<List<Role>> getAllRoles();

    LiveData<List<Role>> getStaffRoles();

    Role getRoleById(int roleId);
}
