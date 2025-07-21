package com.android.coffeeshop.entity;

import com.google.firebase.firestore.PropertyName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    @PropertyName("userId")
    private int userId;

    @PropertyName("roleId")
    private int roleId;
}