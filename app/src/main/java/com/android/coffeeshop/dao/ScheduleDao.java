package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.android.coffeeshop.entity.Schedule;

import java.util.List;

@Dao
public interface ScheduleDao {

    @Query("SELECT * FROM schedule WHERE start_date BETWEEN :startDate AND :endDate")
    LiveData<List<Schedule>> getScheduleData(String startDate, String endDate);

    @Query("SELECT s.* FROM schedule s " +
            "INNER JOIN user u ON s.user_id = u.user_id " +
            "WHERE u.user_name = :userName " +
            "AND s.start_date BETWEEN :startDate AND :endDate")
    LiveData<List<Schedule>> getScheduleOfEmployee(String userName, String startDate, String endDate);

    @Delete
    void delete(Schedule schedule);

    @Insert
    void insert(Schedule schedule);
    @Update
    void update(Schedule schedule);
    @Query("SELECT COUNT(*) FROM schedule WHERE user_id = :userId")
    int countSchedulesByUserId(int userId);
}
