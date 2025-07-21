package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.coffeeshop.entity.User;
import com.android.coffeeshop.utils.EditUserProfile;
import com.android.coffeeshop.utils.StaffWithRole;
import com.android.coffeeshop.utils.UserWithRole;

import java.util.List;

public interface UserDao {
        UserWithRole getUser(String username, String password);

        UserWithRole getUserWithRoleById(int userId);

        LiveData<List<User>> getEmployees();

        // 2: Manager, 3: Employee
        LiveData<List<StaffWithRole>> getStaffList();

        StaffWithRole getStaffById(int userId);

        long insertUser(User user);

        void insertUserRole(int userId, int roleId);

        void updateUser(User user);

        void updateUserRole(int userId, int roleId);

        void deleteUser(int userId);

        void deleteUserRole(int userId);

        User getUserByEmail(String email);

        User getUserByPhone(String phone);

        User getUserByUsername(String username);

        User getUserById(int userId);

        void deleteEmployeeFromUserRole(int userId);

        void deleteEmployeeById(int userId);

        EditUserProfile getUserWithRole(String username);

        void updateUserProfile(String fullName, String phone, String avatar, int userId);

        void updatePassword(String username, String newPassword);

        LiveData<User> getUserSchedule(int userId);
}
