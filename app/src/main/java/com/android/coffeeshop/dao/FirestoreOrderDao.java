package com.android.coffeeshop.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.coffeeshop.dao.BaseDao;
import com.android.coffeeshop.dao.OrderDao;
import com.android.coffeeshop.entity.Order;
import com.android.coffeeshop.utils.DailyOrderStats;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;

public class FirestoreOrderDao implements OrderDao, BaseDao {
    private final FirebaseFirestore db;

    public FirestoreOrderDao(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public LiveData<List<Order>> getOrdersCreateByEmployee(int employeeId) {
        MutableLiveData<List<Order>> liveData = new MutableLiveData<>();
        db.collection("orders").whereEqualTo("userId", employeeId).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Order> orders = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Order order = doc.toObject(Order.class);
                        if (order != null) {
                            order.setOrderId(Integer.parseInt(doc.getId()));
                        }
                        orders.add(order);
                    }
                    liveData.setValue(orders);
                });
        return liveData;
    }

    @Override
    public Order getOrderById(int orderId) {
        DocumentSnapshot doc = executeFirestoreTask(db.collection("orders").document(String.valueOf(orderId)).get());
        if (doc != null) {
            Order order = doc.toObject(Order.class);
            if (order != null) {
                order.setOrderId(Integer.parseInt(doc.getId()));
            }
            return order;
        }
        return null;
    }

    @Override
    public LiveData<List<Order>> getOrdersByDateRange(Date startDate, Date endDate) {
        MutableLiveData<List<Order>> liveData = new MutableLiveData<>();
        db.collection("orders")
                .whereGreaterThanOrEqualTo("createAt", new com.google.firebase.Timestamp(startDate))
                .whereLessThanOrEqualTo("createAt", new com.google.firebase.Timestamp(endDate))
                .orderBy("createAt", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Order> orders = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Order order = doc.toObject(Order.class);
                        if (order != null) {
                            order.setOrderId(Integer.parseInt(doc.getId()));
                        }
                        orders.add(order);
                    }
                    liveData.setValue(orders);
                });
        return liveData;
    }

    @Override
    public LiveData<List<DailyOrderStats>> getDailyOrderStatsByDateRange(Date startDate, Date endDate) {
        MutableLiveData<List<DailyOrderStats>> liveData = new MutableLiveData<>();
        db.collection("orders")
                .whereGreaterThanOrEqualTo("createAt", new com.google.firebase.Timestamp(startDate))
                .whereLessThanOrEqualTo("createAt", new com.google.firebase.Timestamp(endDate))
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Map<String, DailyOrderStats> statsMap = new HashMap<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Order order = doc.toObject(Order.class);
                        if (order != null) {
                            String orderDate = new SimpleDateFormat("yyyy-MM-dd").format(order.getCreateAt().toDate());
                            DailyOrderStats stats = statsMap.getOrDefault(orderDate, new DailyOrderStats(orderDate, 0, 0));
                            stats.setTotalSales(stats.getTotalSales() + order.getTotalPrice());
                            stats.setOrderCount(stats.getOrderCount() + 1);
                            statsMap.put(orderDate, stats);
                        }
                    }
                    liveData.setValue(new ArrayList<>(statsMap.values()));
                });
        return liveData;
    }

    @Override
    public LiveData<Float> getTotalSalesByDateRange(Date startDate, Date endDate) {
        MutableLiveData<Float> liveData = new MutableLiveData<>();
        db.collection("orders")
                .whereGreaterThanOrEqualTo("createAt", new com.google.firebase.Timestamp(startDate))
                .whereLessThanOrEqualTo("createAt", new com.google.firebase.Timestamp(endDate))
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    float totalSales = 0;
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Order order = doc.toObject(Order.class);
                        if (order != null) {
                            totalSales += order.getTotalPrice();
                        }
                    }
                    liveData.setValue(totalSales);
                });
        return liveData;
    }

    @Override
    public int countOrdersByUserId(int userId) {
        QuerySnapshot snapshot = executeFirestoreTask(db.collection("orders").whereEqualTo("userId", userId).get());
        return snapshot != null ? snapshot.size() : 0;
    }
}
