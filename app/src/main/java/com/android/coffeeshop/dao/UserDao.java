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

@Dao
public interface UserDao {
        @Query("SELECT u.user_id AS userId, u.user_name AS userName, u.password AS password, r.role_name AS roleName, u.active AS active "
                        +
                        "FROM user u " +
                        "JOIN user_role ur ON u.user_id = ur.user_id " +
                        "JOIN role r ON ur.role_id = r.role_id " +
                        "WHERE u.user_name = :username AND u.password = :password")
        UserWithRole getUser(String username, String password);

        @Query("SELECT u.user_id AS userId, u.user_name AS userName, u.password AS password, r.role_name AS roleName, u.active AS active "
                        +
                        "FROM user u " +
                        "JOIN user_role ur ON u.user_id = ur.user_id " +
                        "JOIN role r ON ur.role_id = r.role_id " +
                        "WHERE u.user_id = :userId")
        UserWithRole getUserWithRoleById(int userId);

        @Query("SELECT user.* " +
                        "FROM user " +
                        "INNER JOIN user_role ON user.user_id = user_role.user_id " +
                        "INNER JOIN role ON user_role.role_id = role.role_id " +
                        "WHERE role.role_name = 'Employee' " +
                        "ORDER BY user.full_name ASC")
        LiveData<List<User>> getEmployees();

        @Query("SELECT u.user_id, u.full_name, u.email, u.salary, r.role_name " +
                        "FROM user u " +
                        "INNER JOIN user_role ur ON u.user_id = ur.user_id " +
                        "INNER JOIN role r ON ur.role_id = r.role_id " +
                        "WHERE ur.role_id IN (2, 3)")
        // 2: Manager, 3: Employee
        LiveData<List<StaffWithRole>> getStaffList();

        @Query("SELECT u.user_id, u.full_name, u.email, u.salary, r.role_name " +
                        "FROM user u " +
                        "INNER JOIN user_role ur ON u.user_id = ur.user_id " +
                        "INNER JOIN role r ON ur.role_id = r.role_id " +
                        "WHERE u.user_id = :userId")
        StaffWithRole getStaffById(int userId);

        @Insert
        long insertUser(User user);

        @Query("INSERT INTO user_role (user_id, role_id) VALUES (:userId, :roleId)")
        void insertUserRole(int userId, int roleId);

        @Update
        void updateUser(User user);

        @Query("UPDATE user_role SET role_id = :roleId WHERE user_id = :userId")
        void updateUserRole(int userId, int roleId);

        @Query("DELETE FROM user WHERE user_id = :userId")
        void deleteUser(int userId);

        @Query("DELETE FROM user_role WHERE user_id = :userId")
        void deleteUserRole(int userId);

        @Query("SELECT * FROM user WHERE email = :email")
        User getUserByEmail(String email);

        @Query("SELECT * FROM user WHERE phone_number = :phone")
        User getUserByPhone(String phone);

        @Query("SELECT * FROM user WHERE user_name = :username")
        User getUserByUsername(String username);

        @Query("SELECT * FROM user WHERE user_id = :userId")
        User getUserById(int userId);

        @Query("DELETE FROM user_role WHERE user_id = :userId")
        void deleteEmployeeFromUserRole(int userId);

        @Query("DELETE FROM user WHERE user_id = :userId")
        void deleteEmployeeById(int userId);

        @Query("SELECT u.user_id AS userId, u.user_name AS userName,r.role_name AS roleName,u.full_name AS fullName, u.phone_number AS phone ,u.avatar_url AS avatarUrl , u.email AS email "
                        +
                        "FROM user u " +
                        "JOIN user_role ur ON u.user_id = ur.user_id " +
                        "JOIN role r ON ur.role_id = r.role_id " +
                        "WHERE u.user_name = :username")
        EditUserProfile getUserWithRole(String username);

        @Query("UPDATE user " +
                        "SET full_name = :fullName, phone_number = :phone, avatar_url = :avatar " +
                        "WHERE user_id = :userId")
        void updateUserProfile(String fullName, String phone, String avatar, int userId);

        @Query("UPDATE user SET password = :newPassword WHERE user_name = :username")
        void updatePassword(String username, String newPassword);

        @Query("SELECT * FROM user WHERE user_id = :userId")
        LiveData<User> getUserSchedule(int userId);
}
