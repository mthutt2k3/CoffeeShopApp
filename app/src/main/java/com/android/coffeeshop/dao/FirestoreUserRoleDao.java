package com.android.coffeeshop.dao;

import com.android.coffeeshop.entity.UserRole;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreUserRoleDao implements UserRoleDao {
    private final FirebaseFirestore db;

    public FirestoreUserRoleDao(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public void insertUserRole(UserRole userRole) {
        db.collection("user_roles")
                .document(userRole.getUserId() + "_" + userRole.getRoleId())
                .set(userRole);
    }
}