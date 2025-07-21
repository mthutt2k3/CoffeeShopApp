package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.android.coffeeshop.entity.User;
import com.android.coffeeshop.utils.EditUserProfile;
import com.android.coffeeshop.utils.StaffWithRole;
import com.android.coffeeshop.utils.UserWithRole;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class FirestoreUserDao implements UserDao, BaseDao {
    private final FirebaseFirestore db;

    public FirestoreUserDao(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public UserWithRole getUser(String username, String password) {
        QuerySnapshot snapshot = executeFirestoreTask(db.collection("users")
                .whereEqualTo("userName", username)
                .whereEqualTo("password", password)
                .get());
        if (snapshot != null && !snapshot.isEmpty()) {
            DocumentSnapshot doc = snapshot.getDocuments().get(0);
            User user = doc.toObject(User.class);
            if (user != null) {
                user.setUserId(Integer.parseInt(doc.getId()));
                DocumentSnapshot roleDoc = executeFirestoreTask(db.collection("roles")
                        .document(String.valueOf(user.getRoleId()))
                        .get());
                if (roleDoc != null) {
                    String roleName = roleDoc.getString("roleName");
                    return new UserWithRole(
                            user.getUserId(),
                            user.getUserName(),
                            user.getPassword(),
                            roleName,
                            user.isActive() == true ? 1 : 0
                    );
                }
            }
        }
        return null;
    }

    @Override
    public UserWithRole getUserWithRoleById(int userId) {
        DocumentSnapshot doc = executeFirestoreTask(db.collection("users").document(String.valueOf(userId)).get());
        if (doc != null) {
            User user = doc.toObject(User.class);
            if (user != null) {
                user.setUserId(Integer.parseInt(doc.getId()));
                DocumentSnapshot roleDoc = executeFirestoreTask(db.collection("roles").document(String.valueOf(user.getRoleId())).get());
                if (roleDoc != null) {
                    String roleName = roleDoc.getString("roleName");
                    return new UserWithRole(user.getUserId(), user.getUserName(), user.getPassword(), roleName, user.isActive() == true ? 1 : 0);
                }
            }
        }
        return null;
    }

    @Override
    public LiveData<List<User>> getEmployees() {
        MutableLiveData<List<User>> liveData = new MutableLiveData<>();
        db.collection("users").whereEqualTo("roleId", 3).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<User> users = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        User user = doc.toObject(User.class);
                        if (user != null) {
                            user.setUserId(Integer.parseInt(doc.getId()));
                            users.add(user);
                        }
                    }
                    liveData.setValue(users);
                })
                .addOnFailureListener(e -> liveData.setValue(new ArrayList<>()));
        return liveData;
    }

    @Override
    public LiveData<List<StaffWithRole>> getStaffList() {
        MutableLiveData<List<StaffWithRole>> liveData = new MutableLiveData<>();
        db.collection("users").whereIn("roleId", List.of(2, 3)).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<StaffWithRole> staffList = new ArrayList<>();
                    List<CompletableFuture<Void>> futures = new ArrayList<>();

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        User user = doc.toObject(User.class);
                        if (user != null) {
                            user.setUserId(Integer.parseInt(doc.getId()));
                            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                                DocumentSnapshot roleDoc = executeFirestoreTask(db.collection("roles")
                                        .document(String.valueOf(user.getRoleId())).get());
                                if (roleDoc != null) {
                                    String roleName = roleDoc.getString("roleName");
                                    synchronized (staffList) {
                                        staffList.add(new StaffWithRole(
                                                user.getUserId(),
                                                user.getFullName(),
                                                user.getEmail(),
                                                user.getSalary(),
                                                roleName
                                        ));
                                    }
                                }
                            }, Executors.newFixedThreadPool(2));
                            futures.add(future);
                        }
                    }

                    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                            .thenRun(() -> liveData.postValue(staffList))
                            .exceptionally(e -> {
                                e.printStackTrace();
                                liveData.postValue(new ArrayList<>());
                                return null;
                            });
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    liveData.setValue(new ArrayList<>());
                });
        return liveData;
    }

    @Override
    public StaffWithRole getStaffById(int userId) {
        DocumentSnapshot doc = executeFirestoreTask(db.collection("users").document(String.valueOf(userId)).get());
        if (doc != null) {
            User user = doc.toObject(User.class);
            if (user != null) {
                user.setUserId(Integer.parseInt(doc.getId()));
                DocumentSnapshot roleDoc = executeFirestoreTask(db.collection("roles").document(String.valueOf(user.getRoleId())).get());
                if (roleDoc != null) {
                    String roleName = roleDoc.getString("roleName");
                    return new StaffWithRole(user.getUserId(), user.getFullName(), user.getEmail(), user.getSalary(), roleName);
                }
            }
        }
        return null;
    }

    @Override
    public long insertUser(User user) {
        DocumentReference docRef = executeFirestoreTask(db.collection("users").add(user));
        if (docRef != null) {
            return Long.parseLong(docRef.getId());
        }
        return -1;
    }

    @Override
    public void insertUserRole(int userId, int roleId) {
        executeFirestoreTaskVoid(db.collection("users").document(String.valueOf(userId)).update("roleId", roleId));
    }

    @Override
    public void updateUser(User user) {
        executeFirestoreTaskVoid(db.collection("users").document(String.valueOf(user.getUserId())).set(user));
    }

    @Override
    public void updateUserRole(int userId, int roleId) {
        executeFirestoreTaskVoid(db.collection("users").document(String.valueOf(userId)).update("roleId", roleId));
    }

    @Override
    public void deleteUser(int userId) {
        executeFirestoreTaskVoid(db.collection("users").document(String.valueOf(userId)).delete());
    }

    @Override
    public void deleteUserRole(int userId) {
        executeFirestoreTaskVoid(db.collection("users").document(String.valueOf(userId)).update("roleId", null));
    }

    @Override
    public User getUserByEmail(String email) {
        QuerySnapshot snapshot = executeFirestoreTask(db.collection("users").whereEqualTo("email", email).get());
        if (snapshot != null && !snapshot.isEmpty()) {
            DocumentSnapshot doc = snapshot.getDocuments().get(0);
            User user = doc.toObject(User.class);
            if (user != null) {
                user.setUserId(Integer.parseInt(doc.getId()));
            }
            return user;
        }
        return null;
    }

    @Override
    public User getUserByPhone(String phone) {
        QuerySnapshot snapshot = executeFirestoreTask(db.collection("users").whereEqualTo("phoneNumber", phone).get());
        if (snapshot != null && !snapshot.isEmpty()) {
            DocumentSnapshot doc = snapshot.getDocuments().get(0);
            User user = doc.toObject(User.class);
            if (user != null) {
                user.setUserId(Integer.parseInt(doc.getId()));
            }
            return user;
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        QuerySnapshot snapshot = executeFirestoreTask(db.collection("users").whereEqualTo("userName", username).get());
        if (snapshot != null && !snapshot.isEmpty()) {
            DocumentSnapshot doc = snapshot.getDocuments().get(0);
            User user = doc.toObject(User.class);
            if (user != null) {
                user.setUserId(Integer.parseInt(doc.getId()));
            }
            return user;
        }
        return null;
    }

    @Override
    public User getUserById(int userId) {
        DocumentSnapshot doc = executeFirestoreTask(db.collection("users").document(String.valueOf(userId)).get());
        if (doc != null) {
            User user = doc.toObject(User.class);
            if (user != null) {
                user.setUserId(Integer.parseInt(doc.getId()));
            }
            return user;
        }
        return null;
    }

    @Override
    public void deleteEmployeeFromUserRole(int userId) {
        executeFirestoreTaskVoid(db.collection("users").document(String.valueOf(userId)).update("roleId", null));
    }

    @Override
    public void deleteEmployeeById(int userId) {
        executeFirestoreTaskVoid(db.collection("users").document(String.valueOf(userId)).delete());
    }

    @Override
    public EditUserProfile getUserWithRole(String username) {
        QuerySnapshot snapshot = executeFirestoreTask(db.collection("users").whereEqualTo("userName", username).get());
        if (snapshot != null && !snapshot.isEmpty()) {
            DocumentSnapshot doc = snapshot.getDocuments().get(0);
            User user = doc.toObject(User.class);
            if (user != null) {
                user.setUserId(Integer.parseInt(doc.getId()));
                DocumentSnapshot roleDoc = executeFirestoreTask(db.collection("roles").document(String.valueOf(user.getRoleId())).get());
                if (roleDoc != null) {
                    String roleName = roleDoc.getString("roleName");
                    return new EditUserProfile(user.getUserId(), user.getUserName(), roleName, user.getFullName(), user.getPhoneNumber(), user.getAvatarUrl(), user.getEmail());
                }
            }
        }
        return null;
    }

    @Override
    public void updateUserProfile(String fullName, String phone, String avatar, int userId) {
        executeFirestoreTaskVoid(db.collection("users").document(String.valueOf(userId))
                .update("fullName", fullName, "phoneNumber", phone, "avatarUrl", avatar));
    }

    @Override
    public void updatePassword(String username, String newPassword) {
        db.collection("users").whereEqualTo("userName", username).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot doc = queryDocumentSnapshots.getDocuments().get(0);
                        db.collection("users").document(doc.getId()).update("password", newPassword);
                    }
                });
    }

    @Override
    public LiveData<User> getUserSchedule(int userId) {
        MutableLiveData<User> liveData = new MutableLiveData<>();
        db.collection("users").document(String.valueOf(userId)).get()
                .addOnSuccessListener(doc -> {
                    User user = doc.toObject(User.class);
                    if (user != null) {
                        user.setUserId(Integer.parseInt(doc.getId()));
                    }
                    liveData.setValue(user);
                })
                .addOnFailureListener(e -> liveData.setValue(null));
        return liveData;
    }
}