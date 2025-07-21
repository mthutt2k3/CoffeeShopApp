package com.android.coffeeshop.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.coffeeshop.entity.User;
import com.android.coffeeshop.repository.StaffRepository;
import com.android.coffeeshop.utils.StaffWithRole;

import java.util.List;

public class StaffViewModel extends AndroidViewModel {
    private StaffRepository repository;
    private LiveData<List<StaffWithRole>> staffList;

    public StaffViewModel(@NonNull Application application) {
        super(application);
        repository = new StaffRepository(application);
        staffList = repository.getStaffList();
    }

    public LiveData<List<StaffWithRole>> getStaffList() {
        return staffList;
    }

    public void insertStaff(User user, int roleId) {
        repository.insertStaff(user, roleId);
    }

    public void updateStaff(User user, int roleId) {
        repository.updateStaff(user, roleId);
    }

    public void deleteStaff(int userId) {
        repository.deleteStaff(userId);
    }

    public User getUserByEmail(String email) {
        return repository.getUserByEmail(email);
    }

    public User getUserByUsername(String username) {
        return repository.getUserByUsername(username);
    }

    public StaffWithRole getStaffById(int userId) {
        return repository.getStaffById(userId);
    }

    public User getUserById(int userId) {
        return repository.getUserById(userId);
    }
}
