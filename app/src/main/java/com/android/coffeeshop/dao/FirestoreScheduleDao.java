package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.android.coffeeshop.entity.Schedule;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FirestoreScheduleDao implements ScheduleDao, BaseDao {
    private final FirebaseFirestore db;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public FirestoreScheduleDao(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public LiveData<List<Schedule>> getScheduleData(String startDate, String endDate) {
        MutableLiveData<List<Schedule>> liveData = new MutableLiveData<>();
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            QuerySnapshot snapshot = executeFirestoreTask(db.collection("schedules")
                    .whereGreaterThanOrEqualTo("startDate", new com.google.firebase.Timestamp(start))
                    .whereLessThanOrEqualTo("startDate", new com.google.firebase.Timestamp(end))
                    .get());
            List<Schedule> schedules = new ArrayList<>();
            if (snapshot != null) {
                for (DocumentSnapshot doc : snapshot) {
                    Schedule schedule = doc.toObject(Schedule.class);
                    if (schedule != null) {
                        schedule.setScheduleId(Integer.parseInt(doc.getId()));
                    }
                    schedules.add(schedule);
                }
            }
            liveData.setValue(schedules);
        } catch (Exception e) {
            e.printStackTrace();
            liveData.setValue(new ArrayList<>());
        }
        return liveData;
    }

    @Override
    public LiveData<List<Schedule>> getScheduleOfEmployee(String userName, String startDate, String endDate) {
        MutableLiveData<List<Schedule>> liveData = new MutableLiveData<>();
        QuerySnapshot userSnapshot = executeFirestoreTask(db.collection("users").whereEqualTo("userName", userName).get());
        if (userSnapshot != null && !userSnapshot.isEmpty()) {
            String userId = userSnapshot.getDocuments().get(0).getId();
            try {
                Date start = dateFormat.parse(startDate);
                Date end = dateFormat.parse(endDate);
                QuerySnapshot scheduleSnapshot = executeFirestoreTask(db.collection("schedules")
                        .whereEqualTo("userId", Integer.parseInt(userId))
                        .whereGreaterThanOrEqualTo("startDate", new com.google.firebase.Timestamp(start))
                        .whereLessThanOrEqualTo("startDate", new com.google.firebase.Timestamp(end))
                        .get());
                List<Schedule> schedules = new ArrayList<>();
                if (scheduleSnapshot != null) {
                    for (DocumentSnapshot doc : scheduleSnapshot) {
                        Schedule schedule = doc.toObject(Schedule.class);
                        if (schedule != null) {
                            schedule.setScheduleId(Integer.parseInt(doc.getId()));
                        }
                        schedules.add(schedule);
                    }
                }
                liveData.setValue(schedules);
            } catch (Exception e) {
                e.printStackTrace();
                liveData.setValue(new ArrayList<>());
            }
        } else {
            liveData.setValue(new ArrayList<>());
        }
        return liveData;
    }

    @Override
    public void delete(Schedule schedule) {
        executeFirestoreTaskVoid(db.collection("schedules").document(String.valueOf(schedule.getScheduleId())).delete());
    }

    @Override
    public void insert(Schedule schedule) {
        executeFirestoreTaskVoid(db.collection("schedules").document(String.valueOf(schedule.getScheduleId())).set(schedule));
    }

    @Override
    public void update(Schedule schedule) {
        executeFirestoreTaskVoid(db.collection("schedules").document(String.valueOf(schedule.getScheduleId())).set(schedule));
    }

    @Override
    public int countSchedulesByUserId(int userId) {
        QuerySnapshot snapshot = executeFirestoreTask(db.collection("schedules").whereEqualTo("userId", userId).get());
        return snapshot != null ? snapshot.size() : 0;
    }
}