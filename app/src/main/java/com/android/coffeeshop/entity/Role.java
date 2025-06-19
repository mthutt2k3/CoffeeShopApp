package com.android.coffeeshop.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(tableName = "role")

public class Role {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "role_id")
    private int roleId;

    @ColumnInfo(name = "role_name")
    @NonNull
    private String roleName;

    @ColumnInfo(name = "role_description")
    @NonNull
    private String roleDescription;
}
