package com.android.coffeeshop.entity;

import com.google.firebase.firestore.PropertyName;
import com.google.firebase.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    @PropertyName("scheduleId")
    private int scheduleId;

    @PropertyName("userId")
    private int userId;

    @PropertyName("startTime")
    @NonNull
    private Timestamp startTime;

    @PropertyName("endTime")
    @NonNull
    private Timestamp endTime;

    @PropertyName("startDate")
    @NonNull
    private Timestamp startDate;

    @PropertyName("endDate")
    @NonNull
    private Timestamp endDate;
}