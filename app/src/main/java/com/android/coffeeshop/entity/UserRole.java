package com.android.coffeeshop.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity(tableName = "user_role",
        primaryKeys = {"user_id", "role_id"},
        foreignKeys = {
                @ForeignKey(entity = User.class, parentColumns = "user_id", childColumns = "user_id"),
                @ForeignKey(entity = Role.class, parentColumns = "role_id", childColumns = "role_id")
        })

public class UserRole {
    @ColumnInfo(name = "user_id")
    private  int userId;

    @ColumnInfo(name = "role_id")
    private int roleId;
}