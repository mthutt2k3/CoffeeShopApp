package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.android.coffeeshop.entity.Role;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirestoreRoleDao implements RoleDao, BaseDao {
    private final FirebaseFirestore db;

    public FirestoreRoleDao(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public void insertRole(Role role) {
        executeFirestoreTaskVoid(db.collection("roles").document(String.valueOf(role.getRoleId())).set(role));
    }

    @Override
    public Role getRoleByName(String roleName) {
        QuerySnapshot snapshot = executeFirestoreTask(db.collection("roles").whereEqualTo("roleName", roleName).get());
        if (snapshot != null && !snapshot.isEmpty()) {
            DocumentSnapshot doc = snapshot.getDocuments().get(0);
            Role role = doc.toObject(Role.class);
            if (role != null) {
                role.setRoleId(Integer.parseInt(doc.getId()));
            }
            return role;
        }
        return null;
    }

    @Override
    public LiveData<List<Role>> getAllRoles() {
        MutableLiveData<List<Role>> liveData = new MutableLiveData<>();
        db.collection("roles").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Role> roles = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Role role = doc.toObject(Role.class);
                        if (role != null) {
                            role.setRoleId(Integer.parseInt(doc.getId()));
                        }
                        roles.add(role);
                    }
                    liveData.setValue(roles);
                });
        return liveData;
    }

    @Override
    public LiveData<List<Role>> getStaffRoles() {
        MutableLiveData<List<Role>> liveData = new MutableLiveData<>();
        db.collection("roles").whereIn("roleId", List.of(2, 3)).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Role> roles = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Role role = doc.toObject(Role.class);
                        if (role != null) {
                            role.setRoleId(Integer.parseInt(doc.getId()));
                        }
                        roles.add(role);
                    }
                    liveData.setValue(roles);
                });
        return liveData;
    }

    @Override
    public Role getRoleById(int roleId) {
        DocumentSnapshot doc = executeFirestoreTask(db.collection("roles").document(String.valueOf(roleId)).get());
        if (doc != null) {
            Role role = doc.toObject(Role.class);
            if (role != null) {
                role.setRoleId(Integer.parseInt(doc.getId()));
            }
            return role;
        }
        return null;
    }
}