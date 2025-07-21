package com.android.coffeeshop.entity;

import com.google.firebase.firestore.PropertyName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class User {
    @PropertyName("userId")
    int userId;

    @PropertyName("email")
    @NonNull
    String email;

    @PropertyName("userName")
    @NonNull
    String userName;

    @PropertyName("phoneNumber")
    @NonNull
    String phoneNumber;

    @PropertyName("password")
    @NonNull
    String password;

    @PropertyName("fullName")
    String fullName;

    @PropertyName("avatarUrl")
    String avatarUrl;

    @PropertyName("position")
    String position;

    @PropertyName("salary")
    double salary;

    @PropertyName("active")
    @NonNull
    boolean active = true;

    @PropertyName("roleName")
    String roleName;
}