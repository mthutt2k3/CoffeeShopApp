package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.android.coffeeshop.entity.Role;

import java.util.List;

@Dao
public interface RoleDao {

    @Insert
    void insertRole(Role role);

    @Query("SELECT * FROM role WHERE role_name = :roleName")
    Role getRoleByName(String roleName);

    @Query("SELECT * FROM role")
    LiveData<List<Role>> getAllRoles();

    @Query("SELECT * FROM role WHERE role_id IN (2, 3)") // 2: Manager, 3: Employee
    LiveData<List<Role>> getStaffRoles();

    @Query("SELECT * FROM role WHERE role_id = :roleId")
    Role getRoleById(int roleId);
}
