package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.android.coffeeshop.entity.User;
import com.android.coffeeshop.utils.EditUserProfile;
import com.android.coffeeshop.utils.StaffWithRole;
import com.android.coffeeshop.utils.UserWithRole;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.List;

public class FirestoreUserDao implements UserDao {
    private final FirebaseFirestore db;

    public FirestoreUserDao(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public UserWithRole getUser(String username, String password) {
        try {
            QuerySnapshot snapshot = Tasks.await(db.collection("users")
                    .whereEqualTo("userName", username)
                    .whereEqualTo("password", password)
                    .get());
            if (!snapshot.isEmpty()) {
                DocumentSnapshot doc = snapshot.getDocuments().get(0);
                User user = doc.toObject(User.class);
                if (user != null) {
                    user.setUserId(Integer.parseInt(doc.getId()));
                    return new UserWithRole(user.getUserId(), user.getUserName(), user.getPassword(), user.getRoleName(), user.isActive());
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UserWithRole getUserWithRoleById(int userId) {
        try {
            DocumentSnapshot doc = Tasks.await(db.collection("users").document(String.valueOf(userId)).get());
            User user = doc.toObject(User.class);
            if (user != null) {
                user.setUserId(Integer.parseInt(doc.getId()));
                return new UserWithRole(user.getUserId(), user.getUserName(), user.getPassword(), user.getRoleName(), user.isActive());
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public LiveData<List<User>> getEmployees() {
        MutableLiveData<List<User>> liveData = new MutableLiveData<>();
        db.collection("users").whereEqualTo("roleName", "Employee").get()
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
                });
        return liveData;
    }

    @Override
    public LiveData<List<StaffWithRole>> getStaffList() {
        MutableLiveData<List<StaffWithRole>> liveData = new MutableLiveData<>();
        db.collection("users").whereIn("roleName", List.of("Manager", "Employee")).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<StaffWithRole> staffList = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        User user = doc.toObject(User.class);
                        if (user != null) {
                            user.setUserId(Integer.parseInt(doc.getId()));
                            staffList.add(new StaffWithRole(user.getUserId(), user.getFullName(), user.getEmail(), user.getSalary(), user.getRoleName()));
                        }
                    }
                    liveData.setValue(staffList);
                });
        return liveData;
    }

    @Override
    public StaffWithRole getStaffById(int userId) {
        try {
            DocumentSnapshot doc = Tasks.await(db.collection("users").document(String.valueOf(userId)).get());
            User user = doc.toObject(User.class);
            if (user != null) {
                user.setUserId(Integer.parseInt(doc.getId()));
                return new StaffWithRole(user.getUserId(), user.getFullName(), user.getEmail(), user.getSalary(), user.getRoleName());
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long insertUser(User user) {
        try {
            DocumentSnapshot doc = Tasks.await(db.collection("users").add(user));
            return Integer.parseInt(doc.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void insertUserRole(int userId, int roleId) {
        // Lưu roleName trực tiếp trong user, nên không cần user_role
    }

    @Override
    public void updateUser(User user) {
        db.collection("users").document(String.valueOf(user.getUserId())).set(user);
    }

    @Override
    public void updateUserRole(int userId, int roleId) {
        // Cập nhật roleName trong user thay vì user_role
        try {
            DocumentSnapshot roleDoc = Tasks.await(db.collection("roles").document(String.valueOf(roleId)).get());
            String roleName = roleDoc.getString("roleName");
            db.collection("users").document(String.valueOf(userId)).update("roleName", roleName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(int userId) {
        db.collection("users").document(String.valueOf(userId)).delete();
    }

    @Override
    public void deleteUserRole(int userId) {
        // Không cần vì roleName được lưu trong user
    }

    @Override
    public User getUserByEmail(String email) {
        try {
            QuerySnapshot snapshot = Tasks.await(db.collection("users").whereEqualTo("email", email).get());
            if (!snapshot.isEmpty()) {
                DocumentSnapshot doc = snapshot.getDocuments().get(0);
                User user = doc.toObject(User.class);
                if (user != null) {
                    user.setUserId(Integer.parseInt(doc.getId()));
                }
                return user;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getUserByPhone(String phone) {
        try {
            QuerySnapshot snapshot = Tasks.await(db.collection("users").whereEqualTo("phoneNumber", phone).get());
            if (!snapshot.isEmpty()) {
                DocumentSnapshot doc = snapshot.getDocuments().get(0);
                User user = doc.toObject(User.class);
                if (user != null) {
                    user.setUserId(Integer.parseInt(doc.getId()));
                }
                return user;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try {
            QuerySnapshot snapshot = Tasks.await(db.collection("users").whereEqualTo("userName", username).get());
            if (!snapshot.isEmpty()) {
                DocumentSnapshot doc = snapshot.getDocuments().get(0);
                User user = doc.toObject(User.class);
                if (user != null) {
                    user.setUserId(Integer.parseInt(doc.getId()));
                }
                return user;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getUserById(int userId) {
        try {
            DocumentSnapshot doc = Tasks.await(db.collection("users").document(String.valueOf(userId)).get());
            User user = doc.toObject(User.class);
            if (user != null) {
                user.setUserId(Integer.parseInt(doc.getId()));
            }
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteEmployeeFromUserRole(int userId) {
        // Không cần vì roleName được lưu trong user
    }

    @Override
    public void deleteEmployeeById(int userId) {
        db.collection("users").document(String.valueOf(userId)).delete();
    }

    @Override
    public EditUserProfile getUserWithRole(String username) {
        try {
            QuerySnapshot snapshot = Tasks.await(db.collection("users").whereEqualTo("userName", username).get());
            if (!snapshot.isEmpty()) {
                DocumentSnapshot doc = snapshot.getDocuments().get(0);
                User user = doc.toObject(User.class);
                if (user != null) {
                    user.setUserId(Integer.parseInt(doc.getId()));
                    return new EditUserProfile(user.getUserId(), user.getUserName(), user.getRoleName(), user.getFullName(), user.getPhoneNumber(), user.getAvatarUrl(), user.getEmail());
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void updateUserProfile(String fullName, String phone, String avatar, int userId) {
        db.collection("users").document(String.valueOf(userId))
                .update("fullName", fullName, "phoneNumber", phone, "avatarUrl", avatar);
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
                });
        return liveData;
    }
}