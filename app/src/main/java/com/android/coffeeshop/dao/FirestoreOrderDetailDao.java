package com.android.coffeeshop.dao;

import com.android.coffeeshop.entity.OrderDetail;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.List;

public class FirestoreOrderDetailDao implements OrderDetailDao {
    private final FirebaseFirestore db;

    public FirestoreOrderDetailDao(FirebaseFirestore db) {
        this.db = db;
    }

    @Override
    public int countOrderDetailsByProductId(int productId) {
        try {
            QuerySnapshot snapshot = Tasks.await(db.collectionGroup("orderDetails").whereEqualTo("productId", productId).get());
            return snapshot.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        try {
            QuerySnapshot snapshot = Tasks.await(db.collection("orders").document(String.valueOf(orderId))
                    .collection("orderDetails").get());
            List<OrderDetail> orderDetails = new ArrayList<>();
            for (DocumentSnapshot doc : snapshot) {
                OrderDetail detail = doc.toObject(OrderDetail.class);
                if (detail != null) {
                    detail.setOrderDetailId(Integer.parseInt(doc.getId()));
                }
                orderDetails.add(detail);
            }
            return orderDetails;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}