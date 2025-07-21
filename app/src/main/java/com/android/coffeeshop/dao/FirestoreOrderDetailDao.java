package com.android.coffeeshop.dao;

import com.android.coffeeshop.dao.BaseDao;
import com.android.coffeeshop.dao.OrderDetailDao;
import com.android.coffeeshop.entity.OrderDetail;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirestoreOrderDetailDao implements OrderDetailDao, BaseDao {
    private final FirebaseFirestore db;

    public FirestoreOrderDetailDao(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public int countOrderDetailsByProductId(int productId) {
        QuerySnapshot snapshot = executeFirestoreTask(db.collectionGroup("orderDetails").whereEqualTo("productId", productId).get());
        return snapshot != null ? snapshot.size() : 0;
    }

    @Override
    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        QuerySnapshot snapshot = executeFirestoreTask(db.collection("orders").document(String.valueOf(orderId))
                .collection("orderDetails").get());
        if (snapshot != null) {
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (DocumentSnapshot doc : snapshot) {
                OrderDetail detail = doc.toObject(OrderDetail.class);
                if (detail != null) {
                    detail.setOrderDetailId(Integer.parseInt(doc.getId()));
                }
                orderDetails.add(detail);
            }
            return orderDetails;
        }
        return new ArrayList<>();
    }
}
