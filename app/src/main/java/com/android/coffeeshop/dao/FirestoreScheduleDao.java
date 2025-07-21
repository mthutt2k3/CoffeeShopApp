package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.android.coffeeshop.entity.Schedule;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Tasks;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FirestoreScheduleDao implements ScheduleDao {
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
            db.collection("schedules")
                    .whereGreaterThanOrEqualTo("startDate", new com.google.firebase.Timestamp(start))
                    .whereLessThanOrEqualTo("startDate", new com.google.firebase.Timestamp(end))
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        List<Schedule> schedules = new ArrayList<>();
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            Schedule schedule = doc.toObject(Schedule.class);
                            if (schedule != null) {
                                schedule.setScheduleId(Integer.parseInt(doc.getId()));
                            }
                            schedules.add(schedule);
                        }
                        liveData.setValue(schedules);
                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                        liveData.setValue(new ArrayList<>());
                    });
        } catch (Exception e) {
            e.printStackTrace();
            liveData.setValue(new ArrayList<>());
        }
        return liveData;
    }

    @Override
    public LiveData<List<Schedule>> getScheduleOfEmployee(String userName, String startDate, String endDate) {
        MutableLiveData<List<Schedule>> liveData = new MutableLiveData<>();
        try {
            // Lấy userId từ userName
            QuerySnapshot userSnapshot = Tasks.await(db.collection("users").whereEqualTo("userName", userName).get());
            if (!userSnapshot.isEmpty()) {
                String userId = userSnapshot.getDocuments().get(0).getId();
                Date start = dateFormat.parse(startDate);
                Date end = dateFormat.parse(endDate);
                db.collection("schedules")
                        .whereEqualTo("userId", Integer.parseInt(userId))
                        .whereGreaterThanOrEqualTo("startDate", new com.google.firebase.Timestamp(start))
                        .whereLessThanOrEqualTo("startDate", new com.google.firebase.Timestamp(end))
                        .get()
                        .addOnSuccessListener(scheduleSnapshot -> {
                            List<Schedule> schedules = new ArrayList<>();
                            for (DocumentSnapshot doc : scheduleSnapshot) {
                                Schedule schedule = doc.toObject(Schedule.class);
                                if (schedule != null) {
                                    schedule.setScheduleId(Integer.parseInt(doc.getId()));
                                }
                                schedules.add(schedule);
                            }
                            liveData.setValue(schedules);
                        })
                        .addOnFailureListener(e -> {
                            e.printStackTrace();
                            liveData.setValue(new ArrayList<>());
                        });
            } else {
                liveData.setValue(new ArrayList<>());
            }
        } catch (Exception e) {
            e.printStackTrace();
            liveData.setValue(new ArrayList<>());
        }
        return liveData;
    }

    @Override
    public void delete(Schedule schedule) {
        db.collection("schedules").document(String.valueOf(schedule.getScheduleId())).delete();
    }

    @Override
    public void insert(Schedule schedule) {
        db.collection("schedules").document(String.valueOf(schedule.getScheduleId())).set(schedule);
    }

    @Override
    public void update(Schedule schedule) {
        db.collection("schedules").document(String.valueOf(schedule.getScheduleId())).set(schedule);
    }

    @Override
    public int countSchedulesByUserId(int userId) {
        try {
            QuerySnapshot snapshot = Tasks.await(db.collection("schedules").whereEqualTo("userId", userId).get());
            return snapshot.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}