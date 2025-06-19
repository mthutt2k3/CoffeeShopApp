package com.android.coffeeshop.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.android.coffeeshop.entity.Schedule;
import com.android.coffeeshop.entity.User;
import com.android.coffeeshop.repository.ScheduleRepository;

import java.util.List;

import lombok.NonNull;

public class ScheduleViewModel extends AndroidViewModel {
    private final ScheduleRepository scheduleRepository;

    public ScheduleViewModel(@NonNull Application application) {
        super(application);
        scheduleRepository = new ScheduleRepository(application);
    }

    // Method to fetch schedule data
    public LiveData<List<Schedule>> getScheduleData(String startDate, String endDate) {
        return scheduleRepository.getScheduleData(startDate, endDate);
    }

    public LiveData<List<Schedule>> getScheduleOfEmployee(String userName, String startDate, String endDate) {
        return scheduleRepository.getScheduleOfEmployee(userName, startDate, endDate);
    }

    public LiveData<User> getUserById(int userId) {
        return scheduleRepository.getUserById(userId); // Fetch user details by userId
    }

    public void deleteSchedule(Schedule schedule) {
        scheduleRepository.delete(schedule);
    }
    public void updateSchedule(Schedule schedule) {
        scheduleRepository.update(schedule);
    }
    public void addSchedule(Schedule schedule) {
        scheduleRepository.insert(schedule);
    }
}
