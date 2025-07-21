package com.android.coffeeshop.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ImportDataActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        importData();
    }

    private void importData() {
        try {
            WriteBatch batch = db.batch();

            // 1. Roles
            String[][] roles = {
                    {"1", "Storekeeper", "Store owner with the highest authority, managing personnel and finances"},
                    {"2", "Manager", "Managing employees, overseeing daily activities, and coordinating tasks"},
                    {"3", "Employee", "Staff serving customers, making drinks, cashiering, security, and performing daily tasks"}
            };
            for (String[] role : roles) {
                Map<String, Object> roleData = new HashMap<>();
                roleData.put("roleName", role[1]);
                roleData.put("roleDescription", role[2]);
                batch.set(db.collection("roles").document(role[0]), roleData);
            }

            // 2. Users
            String[][] users = {
                    {"1", "khang@coffee.com", "khang", "0123456789", "123456", "Khang", "avatar_url_1", "Cashier", "5000000", "true", "1"},
                    {"2", "phat@coffee.com", "phat", "0123456790", "123456", "Phát", "avatar_url_2", "Barista", "4000000", "false", "2"},
                    {"3", "hai@coffee.com", "hai", "0123456791", "hashed_password_3", "Hải", "avatar_url_3", "Server", "3000000", "true", "3"},
                    {"4", "bach@coffee.com", "bach", "0123456792", "123456", "Bách", "avatar_url_4", "Server", "3000000", "true", "3"},
                    {"5", "minh@coffee.com", "minh", "0123456793", "123456", "Minh", "avatar_url_5", "Security", "3500000", "false", "3"},
                    {"6", "linh@coffee.com", "linh", "0123456794", "123456", "Linh", "avatar_url_6", "Manager", "6000000", "true", "2"},
                    {"7", "hoang@coffee.com", "hoang", "0123456795", "123456", "Hoàng", "avatar_url_7", "Chef", "7000000", "true", "3"},
                    {"8", "tuan@coffee.com", "tuan", "0123456796", "123456", "Tuấn", "avatar_url_8", "Cashier", "4500000", "true", "3"},
                    {"9", "long@coffee.com", "long", "0123456797", "123456", "Long", "avatar_url_9", "Security", "3300000", "true", "3"},
                    {"10", "thao@coffee.com", "thao", "0123456798", "123456", "Thảo", "avatar_url_10", "Employee", "3500000", "true", "3"}
            };
            for (String[] user : users) {
                Map<String, Object> userData = new HashMap<>();
                userData.put("email", user[1]);
                userData.put("userName", user[2]);
                userData.put("phoneNumber", user[3]);
                userData.put("password", user[4]);
                userData.put("fullName", user[5]);
                userData.put("avatarUrl", user[6]);
                userData.put("position", user[7]);
                userData.put("salary", Double.parseDouble(user[8]));
                userData.put("active", Boolean.parseBoolean(user[9]));
                userData.put("roleId", Integer.parseInt(user[10]));
                batch.set(db.collection("users").document(user[0]), userData);
            }

            // 3. Categories
            String[][] categories = {
                    {"1", "Coffee"}, {"2", "Tea"}, {"3", "Pastries"}, {"4", "Snacks"}
            };
            for (String[] category : categories) {
                Map<String, Object> categoryData = new HashMap<>();
                categoryData.put("categoryName", category[1]);
                batch.set(db.collection("categories").document(category[0]), categoryData);
            }

            // 4. Products
            String[][] products = {
                    {"1", "1", "Espresso", "A strong coffee made by forcing hot water through ground coffee beans.", "3.50", "10", null, "2025-03-01 00:00:00", "1"},
                    {"2", "1", "Latte", "A coffee drink made with espresso and milk.", "4.00", "15", null, "2025-03-01 00:00:00", "1"},
                    {"3", "1", "Cappuccino", "A coffee drink made with equal parts of espresso, milk, and foam.", "4.50", "12", null, "2025-03-01 00:00:00", "1"},
                    {"4", "1", "Americano", "A coffee drink made by adding hot water to espresso.", "3.00", "20", null, "2025-03-01 00:00:00", "1"},
                    {"5", "2", "Green Tea", "A tea made from the leaves of the Camellia sinensis plant, known for its health benefits.", "2.50", "25", null, "2025-03-01 00:00:00", "1"},
                    {"6", "2", "Black Tea", "A fully oxidized tea known for its robust flavor.", "2.00", "30", null, "2025-03-01 00:00:00", "1"},
                    {"7", "2", "Chamomile Tea", "A herbal tea made from the flowers of the chamomile plant, known for its relaxing properties.", "3.00", "18", null, "2025-03-01 00:00:00", "1"},
                    {"8", "3", "Croissant", "A flaky, crescent-shaped pastry.", "2.50", "20", null, "2025-03-01 00:00:00", "1"},
                    {"9", "3", "Muffin", "A sweet, baked treat often flavored with fruits or chocolate.", "3.00", "15", null, "2025-03-01 00:00:00", "1"},
                    {"10", "3", "Scone", "A biscuit-like quick bread, often served with tea.", "2.00", "25", null, "2025-03-01 00:00:00", "1"},
                    {"11", "4", "Sandwich", "A dish consisting of two slices of bread with filling in between.", "5.50", "10", null, "2025-03-01 00:00:00", "1"},
                    {"12", "4", "Salad", "A dish of mixed greens and other vegetables, often with a dressing.", "6.00", "8", null, "2025-03-01 00:00:00", "1"},
                    {"13", "4", "Fruit Cup", "A mixture of fresh fruits served in a cup.", "4.50", "12", null, "2025-03-01 00:00:00", "1"}
            };
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            for (String[] product : products) {
                Map<String, Object> productData = new HashMap<>();
                productData.put("categoryId", Integer.parseInt(product[1]));
                productData.put("productName", product[2]);
                productData.put("productRecipes", product[3]);
                productData.put("productPrice", Float.parseFloat(product[4]));
                productData.put("stockQuantity", Integer.parseInt(product[5]));
                productData.put("productImage", product[6]);
                productData.put("createAt", new Timestamp(dateFormat.parse(product[7])));
                productData.put("status", Integer.parseInt(product[8]) == 1);
                batch.set(db.collection("products").document(product[0]), productData);
            }

            // 5. Schedules
            String[][] schedules = {
                    {"1", "1", "2025-03-10 08:00:00", "2025-03-10 16:00:00", "2025-03-10 00:00:00", "2025-03-10 00:00:00"},
                    {"2", "2", "2025-03-10 09:00:00", "2025-03-10 17:00:00", "2025-03-10 00:00:00", "2025-03-10 00:00:00"},
                    {"3", "3", "2025-03-10 07:00:00", "2025-03-10 15:00:00", "2025-03-10 00:00:00", "2025-03-10 00:00:00"},
                    {"4", "4", "2025-03-10 10:00:00", "2025-03-10 18:00:00", "2025-03-10 00:00:00", "2025-03-10 00:00:00"},
                    {"5", "5", "2025-03-10 08:00:00", "2025-03-10 16:00:00", "2025-03-10 00:00:00", "2025-03-10 00:00:00"},
                    {"6", "6", "2025-03-10 09:00:00", "2025-03-10 17:00:00", "2025-03-10 00:00:00", "2025-03-10 00:00:00"},
                    {"7", "7", "2025-03-10 07:00:00", "2025-03-10 15:00:00", "2025-03-10 00:00:00", "2025-03-10 00:00:00"},
                    {"8", "8", "2025-03-10 10:00:00", "2025-03-10 18:00:00", "2025-03-10 00:00:00", "2025-03-10 00:00:00"},
                    {"9", "9", "2025-03-10 08:00:00", "2025-03-10 16:00:00", "2025-03-10 00:00:00", "2025-03-10 00:00:00"},
                    {"10", "1", "2025-03-10 09:00:00", "2025-03-10 17:00:00", "2025-03-10 00:00:00", "2025-03-10 00:00:00"}
            };
            for (String[] schedule : schedules) {
                Map<String, Object> scheduleData = new HashMap<>();
                scheduleData.put("userId", Integer.parseInt(schedule[1]));
                scheduleData.put("startTime", new Timestamp(dateFormat.parse(schedule[2])));
                scheduleData.put("endTime", new Timestamp(dateFormat.parse(schedule[3])));
                scheduleData.put("startDate", new Timestamp(dateFormat.parse(schedule[4])));
                scheduleData.put("endDate", new Timestamp(dateFormat.parse(schedule[5])));
                batch.set(db.collection("schedules").document(schedule[0]), scheduleData);
            }

            // 6. Orders
            String[][] orders = {
                    {"1", "3", "5.0", "150.0", "Pending", "Alice Smith", "Unpaid", "2025-03-22 10:00:00"},
                    {"2", "3", "3.0", "90.0", "Completed", "Bob Johnson", "Paid", "2025-03-21 15:30:00"},
                    {"3", "3", "2.5", "75.0", "Cancelled", "Charlie Brown", "Refunded", "2025-03-20 09:15:00"}
            };
            for (String[] order : orders) {
                Map<String, Object> orderData = new HashMap<>();
                orderData.put("userId", Integer.parseInt(order[1]));
                orderData.put("totalQuantity", Float.parseFloat(order[2]));
                orderData.put("totalPrice", Float.parseFloat(order[3]));
                orderData.put("status", order[4]);
                orderData.put("customer", order[5]);
                orderData.put("paymentStatus", order[6]);
                orderData.put("createAt", new Timestamp(dateFormat.parse(order[7])));
                batch.set(db.collection("orders").document(order[0]), orderData);
            }

            // 7. Order Details
            String[][] orderDetails = {
                    {"1", "3", "6", "4.50"},
                    {"1", "4", "1", "3.00"},
                    {"2", "5", "20", "2.50"},
                    {"2", "6", "20", "2.00"}
            };
            for (String[] detail : orderDetails) {
                Map<String, Object> detailData = new HashMap<>();
                detailData.put("orderId", Integer.parseInt(detail[0]));
                detailData.put("productId", Integer.parseInt(detail[1]));
                detailData.put("quantity", Integer.parseInt(detail[2]));
                detailData.put("price", Float.parseFloat(detail[3]));
                batch.set(db.collection("order_details").document(), detailData);
            }

            // Thực thi batch
            batch.commit()
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Data imported successfully", Toast.LENGTH_LONG).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show());
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}