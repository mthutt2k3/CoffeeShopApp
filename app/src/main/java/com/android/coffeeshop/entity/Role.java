package com.android.coffeeshop.entity;

import com.google.firebase.firestore.PropertyName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @PropertyName("roleId")
    private int roleId;

    @PropertyName("roleName")
    @NonNull
    private String roleName;

    @PropertyName("roleDescription")
    @NonNull
    private String roleDescription;
}