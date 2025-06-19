package com.android.coffeeshop.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.android.coffeeshop.dao.AppDatabase;
import com.android.coffeeshop.dao.ScheduleDao;
import com.android.coffeeshop.dao.UserDao;
import com.android.coffeeshop.entity.Schedule;
import com.android.coffeeshop.entity.User;

import java.util.List;

public class ScheduleRepository {
    private ScheduleDao scheduleDao;
    private UserDao userDao;

    public ScheduleRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        scheduleDao = db.scheduleDao();
        userDao = db.userDao();
    }

    // Get the list of schedules for a user between the given date range
    public LiveData<List<Schedule>> getScheduleData(String startDate, String endDate) {
        return scheduleDao.getScheduleData(startDate, endDate);
    }

    public LiveData<List<Schedule>> getScheduleOfEmployee(String userName, String startDate, String endDate) {
        return scheduleDao.getScheduleOfEmployee(userName, startDate, endDate);
    }

    public LiveData<User> getUserById(int userId) {
        return userDao.getUserSchedule(userId);
    }

    public void delete(Schedule schedule) {
        new Thread(() -> scheduleDao.delete(schedule)).start();
    }
    public void update(Schedule schedule) {
        new Thread(() -> scheduleDao.update(schedule)).start();
    }
    public void insert(Schedule schedule) {
        new Thread(() -> scheduleDao.insert(schedule)).start();
    }
}
