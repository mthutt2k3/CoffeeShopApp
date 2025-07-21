package com.android.coffeeshop.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.android.coffeeshop.entity.User;
import com.android.coffeeshop.repository.UserRepository;
import com.android.coffeeshop.utils.EditUserProfile;
import com.android.coffeeshop.utils.UserWithRole;

import lombok.NonNull;

public class UserViewModel extends AndroidViewModel {
    private final UserRepository userRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
    }

    public UserWithRole login(String username, String password) {
        return userRepository.login(username, password);
    }

    public UserWithRole getUserWithRoleById(int userId) {
        UserWithRole userWithRole = userRepository.getUserWithRoleById(userId);
        return userRepository.getUserWithRoleById(userId);
    }

    public EditUserProfile getUserWithRole(String username) {
        return userRepository.getUserWithRole(username);
    }

    public void updateUserProfile(String fullName, String phone, String avatar, int userId) {
        userRepository.updateUserProfile(fullName, phone, avatar, userId);
    }

    public User getUserByUserName(String userName) {
        return userRepository.getUserByUserName(userName);
    }

    public void changePassword(String username, String newPassword) {
        userRepository.updatePassword(username, newPassword);
    }
}
