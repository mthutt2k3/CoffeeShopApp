package com.android.coffeeshop.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "user_id")
    private   int userId;

    @ColumnInfo(name = "email")
    @NonNull
    private   String email;

    @ColumnInfo(name = "user_name")
    @NonNull
    private  String userName;

    @ColumnInfo(name = "phone_number")
    @NonNull
    private   String phoneNumber;

    @ColumnInfo(name = "password")
    @NonNull
    private    String password;

    @ColumnInfo(name = "full_name")
    private   String fullName;

    @ColumnInfo(name = "avatar_url")
    private   String avatarUrl;

    @ColumnInfo(name = "position")
    private   String position;

    @ColumnInfo(name = "salary")
    private   double salary;

    @ColumnInfo(name = "active")
    @NonNull
    private boolean active = true;
}
