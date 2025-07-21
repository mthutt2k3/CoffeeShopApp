package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import com.android.coffeeshop.entity.Schedule;

import java.util.List;

public interface ScheduleDao {
    LiveData<List<Schedule>> getScheduleData(String startDate, String endDate);

    LiveData<List<Schedule>> getScheduleOfEmployee(String userName, String startDate, String endDate);

    void delete(Schedule schedule);

    void insert(Schedule schedule);

    void update(Schedule schedule);

    int countSchedulesByUserId(int userId);
}